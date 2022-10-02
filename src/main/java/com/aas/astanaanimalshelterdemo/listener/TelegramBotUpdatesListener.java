package com.aas.astanaanimalshelterdemo.listener;

import com.aas.astanaanimalshelterdemo.botModel.*;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.AdoptiveParentsMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.ArrangementCatMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.ArrangementDogMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.CynologistMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.InfoMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.StartMenuEnum;
import com.aas.astanaanimalshelterdemo.botService.AvatarService;
import com.aas.astanaanimalshelterdemo.botService.CatUsersService;
import com.aas.astanaanimalshelterdemo.botService.DogUsersService;
import com.aas.astanaanimalshelterdemo.botService.InfoService;
import com.aas.astanaanimalshelterdemo.botService.PetService;
import com.aas.astanaanimalshelterdemo.botService.ReportService;
import com.aas.astanaanimalshelterdemo.botService.UsersService;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.mock.web.MockMultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.username}")
    private String username;
    private final InfoService infoService;
    private final PetService petService;
    private final AvatarService avatarService;
    private final DogUsersService dogUsersService;
    private final CatUsersService catUsersService;
    private final ReportService reportService;
    private final UsersService usersService;

    public TelegramBotUpdatesListener(InfoService infoService,
                                      PetService petService,
                                      AvatarService avatarService,
                                      DogUsersService dogUsersService,
                                      CatUsersService catUsersService,
                                      ReportService reportService, UsersService usersService) {
        this.infoService = infoService;
        this.petService = petService;
        this.avatarService = avatarService;
        this.dogUsersService = dogUsersService;
        this.catUsersService = catUsersService;
        this.reportService = reportService;
        this.usersService = usersService;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            try {
                handleCallBack(update.getCallbackQuery());
            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException, IOException {
        AnimalType type = checkedType(message);
        if (message.hasText()) {
            if ("/start".equals(message.getText())) {
                chooseMenu(message, type);
            } else if (message.getText().startsWith("Рацион питания")) {
                setPetDiet(message);
            } else if (message.getText().startsWith("Общее самочувствие")) {
                Users user = usersService.getUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                List<Report> reports = reportService.getReportsByUser(user);
                Report report = new Report();
                for (Report r : reports) {
                    if (r.getDataTime().toLocalDate().equals(LocalDate.now())) {
                        report = r;
                    }
                }
                report.setStateOfHealth(message.getText());
                reportService.save(report);
                execute(SendMessage.builder()
                        .text("Опишите пожалуйста изменения в поведении питомца.\n" +
                              "Начните со слов Поведение питомца.")
                        .chatId(message.getChatId()).build());
            } else if (message.getText().startsWith("Поведение питомца")) {
                Users user = usersService.getUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                List<Report> reports = reportService.getReportsByUser(user);
                Report report = new Report();
                for (Report r : reports) {
                    if (r.getDataTime().toLocalDate().equals(LocalDate.now())) {
                        report = r;
                    }
                }
                report.setHabits(message.getText());
                reportService.save(report);
                execute(SendMessage.builder()
                        .text("Загрузите пожалуйста актуальную фотографию питомца.")
                        .chatId(message.getChatId()).build());
            } else if (message.getText().matches("([0-9.:\\s]{16})")) {
                checkDate(message, type);
            } else if (message.getText().matches(
                    "^((\\+7\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{2}[- .]?\\d{2}$")) {
                checkedUserForPhone(message, type);
            } else if (message.getText().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
                checkedUserForEmail(message, type);
            } else {
                wrongMessage(message);
            }
        } else if (message.hasPhoto()) {
            Users user = usersService.getUsersByChatId(message.getChatId())
                    .orElseThrow(NotFoundException::new);
            Pet pet = petService.getPetByUsers(user)
                    .orElseThrow(NotFoundException::new);

            List<PhotoSize> photo = message.getPhoto();
            String fileId = Objects.requireNonNull(photo.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();

            URL url = new URL("https://api.telegram.org/bot"
                              + getBotToken() + "/getFile?file_id=" + fileId);

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String getFileResponse = br.readLine();
            JSONObject jresult = new JSONObject(getFileResponse);
            JSONObject path = jresult.getJSONObject("result");
            String filePath = path.getString("file_path");

            File file = downloadFile(filePath);
            FileInputStream is = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("photo.jpg",
                    "photo.jpg", "image/jpeg",
                    IOUtils.toByteArray(is));

            avatarService.upLoadAvatar(pet.getId(), multipartFile);

            List<Report> reports = reportService.getReportsByUser(user);
            Report report = new Report();
            for (Report r : reports) {
                if (r.getDataTime().toLocalDate().equals(LocalDate.now())) {
                    report = r;
                }
            }

            List<Avatar> avatars = avatarService.getAvatarsByPetId(pet.getId());
            Avatar avatar = avatars.stream()
                    .max(Comparator.comparing(Avatar::getId))
                    .orElseThrow(NotFoundException::new);
            avatar.setReport(report);
            avatarService.save(avatar);

            if (!(report.getDiet() == null && report.getStateOfHealth() == null
                  && report.getHabits() == null)) {
                execute(SendMessage.builder().text("Ваш отчет принят.")
                        .chatId(message.getChatId()).build());
                processingStartMenu(message, pet.getTypeOfAnimal());
            }
        }
    }

    private void setPetDiet(Message message) throws TelegramApiException {
        Report report = new Report();
        Users user = usersService.getUsersByChatId(message.getChatId())
                .orElseThrow(NotFoundException::new);
        Pet pet = petService.getPetByUsers(user)
                .orElseThrow(NotFoundException::new);
        report.setDataTime(LocalDateTime.now());
        report.setUser(user);
        report.setPet(pet);
        report.setDiet(message.getText());
        reportService.save(report);
        execute(SendMessage.builder()
                .text("Опишите пожалуйста общее самочувствие питомца.\n" +
                      "Начните со слов Общее самочувствие.")
                .chatId(message.getChatId()).build());
    }

    private void checkDate(Message message, AnimalType type) throws TelegramApiException {
        LocalDateTime dateTimeOfVisit = parseDateTime(message.getText());
        if (Objects.isNull(dateTimeOfVisit)) {
            execute(SendMessage.builder()
                    .text("Некорректный формат даты и/или времени.\n"
                          + "Введите дату и время в формате:\n"
                          + MessagesForUsers.FORMAT_DATE_TIME)
                    .chatId(message.getChatId()).build());
        } else {
            if (type != null) {
                checkedUserForDate(message, type, dateTimeOfVisit);
            }
        }
    }

    private void chooseMenu(Message message, AnimalType type) throws TelegramApiException {
        if (type != null) {
            processingStartMenu(message, type);
        } else {
            processingMenu(message);
        }
    }

    private AnimalType checkedType(Message message) {
        AnimalType type = null;
        Optional<CatUsers> catUsers = catUsersService.getUserByChatId(message.getChatId());
        Optional<DogUsers> dogUsers = dogUsersService.getUserByChatId(message.getChatId());
        if (dogUsers.isPresent()) {
            type = AnimalType.DOG;
        } else if (catUsers.isPresent()) {
            type = AnimalType.CAT;
        }
        return type;
    }

    private void checkedUserForEmail(Message message, AnimalType type) throws TelegramApiException {
        if (type == AnimalType.DOG) {
            List<List<InlineKeyboardButton>> buttons = getStartButton(AnimalType.DOG);
            execute(SendMessage.builder()
                    .text("Спасибо. Выберете дальнейшее действие.")
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
            dogUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setEmailAddress(message.getText());
                dogUsersService.save(user);
            });
        } else {
            List<List<InlineKeyboardButton>> buttons = getStartButton(AnimalType.CAT);
            execute(SendMessage.builder()
                    .text("Спасибо. Выберете дальнейшее действие.")
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
            catUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setEmailAddress(message.getText());
                catUsersService.save(user);
            });
        }
    }

    private void checkedUserForPhone(Message message, AnimalType type) throws TelegramApiException {
        if (type == AnimalType.DOG) {
            dogUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setPhoneNumber(message.getText());
                dogUsersService.save(user);
            });
        } else {
            catUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setPhoneNumber(message.getText());
                catUsersService.save(user);
            });
        }
        execute(SendMessage.builder()
                .text("Введите пожалуйста адрес Вашей электронной почты.")
                .chatId(message.getChatId()).build());
    }

    private void checkedUserForDate(Message message, AnimalType type, LocalDateTime dateTimeOfVisit) throws TelegramApiException {
        if (type == AnimalType.DOG) {
            execute(SendMessage.builder()
                    .text("Спасибо. Мы будем Вас ждать в нашем приюте \n"
                          + dateTimeOfVisit)
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboard(getStartButton(AnimalType.DOG)).build()).build());
            dogUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setDataTimeOfPet(dateTimeOfVisit);
                dogUsersService.save(user);
            });
        } else {
            execute(SendMessage.builder()
                    .text("Спасибо. Мы будем Вас ждать в нашем приюте \n"
                          + dateTimeOfVisit)
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboard(getStartButton(AnimalType.CAT)).build()).build());
            catUsersService.getUserByChatId(message.getChatId()).ifPresent(user -> {
                user.setDataTimeOfPet(dateTimeOfVisit);
                catUsersService.save(user);
            });
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException,
            IOException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Long petId = param.length == 3 ? Long.valueOf(param[2]) : null;
        AnimalType type = param.length >= 2 ? AnimalType.valueOf(param[1]) : null;
        Long infoId = type == AnimalType.DOG ? 1L : 2L;
        switch (action) {
            case "DOG", "CAT" -> processingStartMenu(message, type);
            case "START" -> checkedStart(message, type);
            case "YES" -> checkedExit(message, type);
            case "NO", "INFORMATION" -> processingInfoMenu(message, type);
            case "TAKE" -> processingAdoptiveParentsMenu(message, type);
            case "REPORT" -> reportLoad(message, type);
            case "CALL" -> execute(SendMessage.builder()
                    .text("Вызов волонтера")
                    .chatId(message.getChatId().toString())
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
            case "WORKING" -> infoLoad(message, type, infoId);
            case "CONTACT" -> contactLoad(message, type);
            case "SAFETY" -> safetyLoad(message, type, infoId);
            case "ARRANGEMENT" -> processingArrangementMenu(message, type);
            case "CHOOSING" -> choosingOfPet(message, type);
            case "RULES" -> rulesLoad(message, type, infoId);
            case "DOCUMENTS" -> documentsLoad(message, type, infoId);
            case "TRANSPORTATION" -> transportationLoad(message, type, infoId);
            case "ADVICE" -> adviceLoad(message, type, infoId);
            case "CYNOLOGIST" -> cynologistLoad(message, type, infoId);
            case "REFUSAL" -> refusalLoad(message, type, infoId);
            case "PUPPIES", "KITTENS" -> adviceForHomeForBabyLoad(message, type, infoId);
            case "DOG_ADULT", "CAT_ADULT" -> adviceForHomeForAdultPetLoad(message, type, infoId);
            case "DOG_LIMITED", "CAT_LIMITED" -> adviceForHomeForPetWithDisabilityLoad(message, type, infoId);
            case "TAKING" -> chooseTaking(message, petId, type);
        }
    }

    private void reportLoad(Message message, AnimalType type) throws TelegramApiException {
        if (dogUsersService.getUserByChatId(message.getChatId()).isPresent()
            && dogUsersService.getUserByChatId(message.getChatId())
                    .get().getRole().equals(UserType.OWNER)
            || catUsersService.getUserByChatId(message.getChatId()).isPresent()
               && catUsersService.getUserByChatId(message.getChatId())
                       .get().getRole().equals(UserType.OWNER)) {
            execute(SendMessage.builder()
                    .text("Вам необходимо описать рацион питания, общее самочувствие, " +
                          "изменения в поведении и загрузить актуальную фотографию" +
                          "Вашего питомца.")
                    .chatId(message.getChatId().toString())
                    .build());
            execute(SendMessage.builder()
                    .text("Опишите пожалуйста рацион питания питомца.\n" +
                          "Начните со слов Рацион питания.")
                    .chatId(message.getChatId().toString())
                    .build());
        } else {
            execute(SendMessage.builder()
                    .text("У вас еще нет питомца!")
                    .chatId(message.getChatId().toString())
                    .build());
            processingStartMenu(message, type);
        }
    }

    private void contactLoad(Message message, AnimalType type) throws TelegramApiException {
        if (type != null) {
            execute(SendMessage.builder()
                    .text("Введите номер Вашего телефона в формате: \n" +
                          MessagesForUsers.FORMAT_PHONE_NUMBER)
                    .chatId(message.getChatId().toString())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(getStartButton(type)).build())
                    .build());
        }
    }

    private void adviceForHomeForPetWithDisabilityLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getAdviceForHomeForPetWithDisability())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void adviceForHomeForAdultPetLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getAdviceForHomeForAdultPet())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void adviceForHomeForBabyLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getAdviceForHomeForBaby())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void chooseTaking(Message message, Long petId, AnimalType type) throws TelegramApiException {
        if (type == AnimalType.DOG) {
            if (catUsersService.getUserByChatId(message.getChatId()).isEmpty()
                && dogUsersService.getUserByChatId(message.getChatId()).isEmpty()) {
                takingDog(message, type, petId);
            } else {
                execute(SendMessage.builder()
                        .text("У вас уже есть животное. Приходите через 30 дней")
                        .chatId(message.getChatId())
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboard(getStartButton(type)).build())
                        .build());
            }
        } else {
            if (dogUsersService.getUserByChatId(message.getChatId()).isEmpty()
                && catUsersService.getUserByChatId(message.getChatId()).isEmpty()) {
                takingCat(message, type, petId);
            } else {
                execute(SendMessage.builder()
                        .text("У вас уже есть животное. Приходите через 30 дней")
                        .chatId(message.getChatId())
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboard(getStartButton(type)).build())
                        .build());
            }
        }
    }

    private void refusalLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getReasonsForRefusal())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void cynologistLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getListOfDogHandler())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void adviceLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getTipsOfDogHandler())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void transportationLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getAdviceForTransporting())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void documentsLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getListOfDocuments())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void rulesLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getDatingRules())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void safetyLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getSafetyPrecautions())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void infoLoad(Message message, AnimalType type, Long infoId) throws TelegramApiException, IOException {
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = getButtons(type);
        execute(SendMessage.builder()
                .text(info.getWorkMode() + "\n"
                      + info.getAddress() + "\n"
                      + info.getContacts())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
        File file = File.createTempFile("location", "jpg");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(info.getLocation());
        }
        InputFile inputFile = new InputFile(file);
        execute(SendPhoto.builder()
                .photo(inputFile)
                .chatId(message.getChatId().toString())
                .build());
    }

    private void checkedExit(Message message, AnimalType type) throws TelegramApiException {
        if (type == AnimalType.DOG) {
            if (dogUsersService.getUserByChatId(message.getChatId()).isPresent()) {
                dogUsersService.getUserByChatId(message.getChatId()).ifPresent(dogUser -> {
                    if (dogUser.getRole() == UserType.USER) {
                        dogUsersService.delete(dogUser);
                    }
                });
            }
        } else {
            if (catUsersService.getUserByChatId(message.getChatId()).isPresent()) {
                catUsersService.getUserByChatId(message.getChatId()).ifPresent(catUser -> {
                    if (catUser.getRole() == UserType.USER) {
                        catUsersService.delete(catUser);
                    }
                });
            }
        }
        processingMenu(message);
    }

    private void checkedStart(Message message, AnimalType type) throws TelegramApiException {
        if (dogUsersService.getUserByChatId(message.getChatId()).isPresent()
            || catUsersService.getUserByChatId(message.getChatId()).isPresent()) {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("Да")
                            .callbackData("YES:" + type)
                            .build()));
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("Нет")
                            .callbackData("NO:" + type)
                            .build()));
            execute(SendMessage.builder()
                    .text("При переходе к выбору приюта все ваши данные будут удалены!")
                    .chatId(message.getChatId().toString())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());

        } else {
            processingMenu(message);
        }
    }

    @Nullable
    private LocalDateTime parseDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static List<List<InlineKeyboardButton>> getButtons(AnimalType type) {
        List<List<InlineKeyboardButton>> buttons = getStartButton(type);
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name() + ":" + type)
                        .build()));
        return buttons;
    }

    private static List<List<InlineKeyboardButton>> getStartButton(AnimalType type) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Перейти к стартовому меню")
                        .callbackData(type.name() + ":" + type)
                        .build()));
        return buttons;
    }

    private void takingDog(Message message, AnimalType type, Long petId) throws TelegramApiException {
        DogUsers dogUsers = dogUsersService.getUserByChatId(message.getChatId())
                .orElseThrow(NotFoundException::new);
        if (dogUsers.getPhoneNumber() == null) {
            messageForContact(message, type);
        } else {
            Optional<Pet> pet = petService.getPetByPetId(petId);
            if (pet.isPresent()) {
                pet.get().setUsers(dogUsers);
                dogUsers.setPet(pet.get());
                dogUsers.setRole(UserType.OWNER);
                dogUsersService.save(dogUsers);
                petService.save(pet.get());
                Info info = infoService.getInfo(1L);
                execute(SendMessage.builder().text(MessagesForUsers.MESSAGE_FOR_NEW_TUTOR +
                                                   "\n" + info.getWorkMode() + "по адресу: " +
                                                   info.getAddress() +
                                                   "\n\n Введите пожалуйста дату и время, " +
                                                   "когда Вы сможете посетить наш приют, в формате: \n" +
                                                   MessagesForUsers.FORMAT_DATE_TIME)
                        .chatId(message.getChatId())
                        .build());
            } else {
                wrongMessage(message);
            }
        }
    }

    private void takingCat(Message message, AnimalType type, Long petId) throws TelegramApiException {
        CatUsers catUser = catUsersService.getUserByChatId(message.getChatId())
                .orElseThrow(NotFoundException::new);
        if (catUser.getPhoneNumber() == null) {
            messageForContact(message, type);
        } else {
            Optional<Pet> pet = petService.getPetByPetId(petId);
            if (pet.isPresent()) {
                pet.get().setUsers(catUser);
                catUser.setPet(pet.get());
                catUser.setRole(UserType.OWNER);
                catUsersService.save(catUser);
                petService.save(pet.get());
                Info info = infoService.getInfo(1L);
                execute(SendMessage.builder().text(MessagesForUsers.MESSAGE_FOR_NEW_TUTOR +
                                                   "\n" + info.getWorkMode() + "по адресу: " +
                                                   info.getAddress() +
                                                   "\n\n Введите пожалуйста дату и время, " +
                                                   "когда Вы сможете посетить наш приют, в формате: \n" +
                                                   MessagesForUsers.FORMAT_DATE_TIME)
                        .chatId(message.getChatId())
                        .build());
            } else {
                wrongMessage(message);
            }
        }
    }

    private void wrongMessage(Message message) throws TelegramApiException {
        execute(SendMessage.builder()
                .text("Вы ввели неверные данные. Повторите еще раз.")
                .chatId(message.getChatId()).build());
    }

    private void messageForContact(Message message, AnimalType type) throws TelegramApiException {
        List<List<InlineKeyboardButton>> button = new ArrayList<>();
        button.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(InfoMenuEnum.CONTACT.getInfo())
                        .callbackData(InfoMenuEnum.CONTACT.name() + ":" + type)
                        .build()));
        execute(SendMessage.builder().text("Вам необходимо зарегистрироваться")
                .chatId(message.getChatId())
                .replyMarkup(InlineKeyboardMarkup.builder().
                        keyboard(button).build())
                .build());
    }

    private void choosingOfPet(Message message, AnimalType type) throws TelegramApiException,
            FileNotFoundException {
        List<Pet> petList = petService.getPetsByTypeOfAnimal(type);
        for (Pet pet : petList) {
            List<Avatar> avatars = avatarService.getAvatarsByPetId(pet.getId());
            File image = ResourceUtils.getFile(avatars.get(0).getFilePath());
            InputFile inputFile = new InputFile(image, image.getName());
            List<List<InlineKeyboardButton>> button = new ArrayList<>();
            button.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("Выбрать питомца " + pet.getId())
                            .callbackData("TAKING:" + type + ":" + pet.getId())
                            .build()));
            execute(SendMessage.builder()
                    .text("id: " + pet.getId() + "\nИмя: " + pet.getName()
                          + "\nВозраст: " + pet.getAge())
                    .chatId(message.getChatId().toString())
                    .build());
            execute(SendPhoto.builder()
                    .photo(inputFile)
                    .chatId(message.getChatId().toString())
                    .replyMarkup(InlineKeyboardMarkup.builder().
                            keyboard(button).build())
                    .build());
        }
        execute(SendMessage.builder()
                .text("Для отмены перейдите в стартовое меню")
                .chatId(message.getChatId())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(getStartButton(type)).build())
                .build());
    }

    private void processingArrangementMenu(Message message, AnimalType type) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if (type == AnimalType.DOG) {
            for (ArrangementDogMenuEnum menu : ArrangementDogMenuEnum.values()) {
                buttons.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(menu.getInfo())
                                .callbackData(menu.name() + ":" + type)
                                .build()));
            }
        } else {
            for (ArrangementCatMenuEnum menu : ArrangementCatMenuEnum.values()) {
                buttons.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(menu.getInfo())
                                .callbackData(menu.name() + ":" + type)
                                .build()));
            }
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name() + ":" + type)
                        .build()));

        execute(SendMessage.builder()
                .text("Выберите категорию животного")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingAdoptiveParentsMenu(Message message, AnimalType type) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (AdoptiveParentsMenuEnum menu : AdoptiveParentsMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name() + ":" + type)
                            .build()));
        }
        if (type == AnimalType.DOG) {
            for (CynologistMenuEnum menu : CynologistMenuEnum.values()) {
                buttons.add(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(menu.getInfo())
                                .callbackData(menu.name() + ":" + type)
                                .build()));
            }
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(InfoMenuEnum.CONTACT.getInfo())
                        .callbackData(InfoMenuEnum.CONTACT.name() + ":" + type)
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CALL.getInfo())
                        .callbackData(StartMenuEnum.CALL.name() + ":" + type)
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name() + ":" + type)
                        .build()));
        execute(SendMessage.builder()
                .text("Подбор животного")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingInfoMenu(Message message, AnimalType type) throws TelegramApiException {
        Long infoId = type == AnimalType.DOG ? 1L : 2L;
        Info info = infoService.getInfo(infoId);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (InfoMenuEnum menu : InfoMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name() + ":" + type)
                            .build()));
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CALL.getInfo())
                        .callbackData(StartMenuEnum.CALL.name() + ":" + type)
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name() + ":" + type)
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Вернутся к выбору приюта?")
                        .callbackData("START:" + type)
                        .build()));
        execute(SendMessage.builder()
                .text(info.getAboutShelter())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingStartMenu(Message message, AnimalType type) throws TelegramApiException {
        checkedUser(message, type);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (StartMenuEnum menu : StartMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name() + ":" + type)
                            .build()));
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Вернутся к выбору приюта?")
                        .callbackData("START:" + type)
                        .build()));
        execute(SendMessage.builder()
                .text(message.getFrom().getUserName() + ", приветствуем вас в нашем приюте!")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void checkedUser(Message message, AnimalType type) {
        if (dogUsersService.getUserByChatId(message.getChatId()).isEmpty()
            && catUsersService.getUserByChatId(message.getChatId()).isEmpty()) {
            if (type == AnimalType.DOG) {
                DogUsers user = new DogUsers();
                user.setUserName(message.getFrom().getUserName());
                user.setChatId(message.getChatId());
                user.setRole(UserType.USER);
                dogUsersService.save(user);
            } else {
                CatUsers user = new CatUsers();
                user.setUserName(message.getFrom().getUserName());
                user.setChatId(message.getChatId());
                user.setRole(UserType.USER);
                catUsersService.save(user);
            }
        }
    }

    private void processingMenu(Message message) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Приют для Собак")
                        .callbackData("DOG:" + AnimalType.DOG.name())
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Приют для кошек")
                        .callbackData("CAT:" + AnimalType.CAT.name())
                        .build()));
        execute(SendMessage.builder()
                .text("Добро пожаловать в приют!")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }
    @Scheduled(cron = "0 18 13 * * *")
    public void sendNotification() throws TelegramApiException {
        List<Users> usersWithPet = usersService.getUsersWithPet();
        for (Users user : usersWithPet) {
            dogUsersService.getUserById(user.getId()).ifPresent(dogUsers -> {
                if (dogUsers.getRole().equals(UserType.OWNER)
                    && reportService.getReportsByUser(dogUsers).isEmpty()) {
                    try {
                        execute(SendMessage.builder()
                                .text("Вы сегодня не прислали отчет о питомце "
                                      + dogUsers.getPet().getName() + ".\n" +
                                      "Настоятельно рекомендуем Вам срочно прислать отчет.")
                                .chatId(user.getChatId())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Collections.singletonList(
                                        Collections.singletonList(
                                                InlineKeyboardButton.builder()
                                                        .text(StartMenuEnum.REPORT.getInfo())
                                                        .callbackData(StartMenuEnum.REPORT.name() + ":" +
                                                                      AnimalType.DOG.name())
                                                        .build())
                                )).build())
                                .build());

                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
            catUsersService.getUserById(user.getId()).ifPresent(catUsers -> {
                if (catUsers.getRole().equals(UserType.OWNER)
                    && reportService.getReportsByUser(catUsers).isEmpty()) {
                    try {
                        execute(SendMessage.builder()
                                .text("Вы сегодня не прислали отчет о питомце "
                                      + catUsers.getPet().getName() + ".\n" +
                                      "Настоятельно рекомендуем Вам срочно прислать отчет.")
                                .chatId(user.getChatId())
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Collections.singletonList(
                                        Collections.singletonList(
                                                InlineKeyboardButton.builder()
                                                        .text(StartMenuEnum.REPORT.getInfo())
                                                        .callbackData(StartMenuEnum.REPORT.name() + ":" +
                                                                      AnimalType.CAT.name())
                                                        .build())
                                )).build())
                                .build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            List<Report> reports = reportService.getReportsByUser(user);
            if (!reports.isEmpty()) {
                Report lastReport = reports.stream()
                        .max(Comparator.comparing(Report::getId))
                        .orElseThrow(NotFoundException::new);
                if (!(lastReport.getDataTime().toLocalDate().equals(LocalDate.now())
                      && reports.size() < 30)) {
                    execute(SendMessage.builder()
                            .text("Вы сегодня не прислали отчет о питомце " +
                                  lastReport.getPetId().getName() + ".\n" +
                                  "Настоятельно рекомендуем Вам срочно прислать отчет.")
                            .chatId(user.getChatId())
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(Collections.singletonList(
                                    Collections.singletonList(
                                            InlineKeyboardButton.builder()
                                                    .text(StartMenuEnum.REPORT.getInfo())
                                                    .callbackData(StartMenuEnum.REPORT.name() + ":" +
                                                                  lastReport.getPetId()
                                                                          .getTypeOfAnimal().name())
                                                    .build())
                            )).build())
                            .build());
                }
            }
        }
    }
}
