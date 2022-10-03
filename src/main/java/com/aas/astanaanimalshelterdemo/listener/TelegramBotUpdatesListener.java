package com.aas.astanaanimalshelterdemo.listener;

import com.aas.astanaanimalshelterdemo.botModel.*;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.AdoptiveParentsMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.ArrangementMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.InfoMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.StartMenuEnum;
import com.aas.astanaanimalshelterdemo.botRepositories.AvatarRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.ReportRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.UsersRepository;
import com.aas.astanaanimalshelterdemo.botService.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.ws.rs.NotFoundException;
import java.io.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
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
    private final UsersRepository usersRepository;
    private final PetRepository petRepository;
    private final ReportRepository reportRepository;
    private final AvatarRepository avatarRepository;
    private final UsersService usersService;
    private final ReportService reportService;

    public TelegramBotUpdatesListener(InfoService infoService,
                                      PetService petService,
                                      AvatarService avatarService,
                                      UsersRepository usersRepository,
                                      PetRepository petRepository,
                                      ReportRepository reportRepository,
                                      AvatarRepository avatarRepository,
                                      UsersService usersService,
                                      ReportService reportService) {
        this.infoService = infoService;
        this.petService = petService;
        this.avatarService = avatarService;
        this.usersRepository = usersRepository;
        this.petRepository = petRepository;
        this.reportRepository = reportRepository;
        this.avatarRepository = avatarRepository;
        this.usersService = usersService;
        this.reportService = reportService;
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
        if (message.hasText()) {
            if ("/start".equals(message.getText())) {
                processingStartMenu(message);
            } else if (message.getText().matches("([0-9.:\\s]{16})")) {
                LocalDateTime dateTimeOfVisit = parseDateTime(message.getText());
                if (Objects.isNull(dateTimeOfVisit)) {
                    execute(SendMessage.builder()
                            .text("Некорректный формат даты и/или времени.\n"
                                    + "Введите дату и время в формате:\n"
                                    + MessagesForUsers.FORMAT_DATE_TIME)
                            .chatId(message.getChatId()).build());
                } else {
                    execute(SendMessage.builder()
                            .text("Спасибо. Мы будем Вас ждать в нашем приюте \n"
                                    + dateTimeOfVisit)
                            .chatId(message.getChatId())
                            .replyMarkup(InlineKeyboardMarkup.builder()
                                    .keyboard(getStartButton()).build()).build());
                    Users user = usersRepository.findUsersByChatId(message.getChatId())
                            .orElseThrow(NotFoundException::new);
                    user.setDataTimeOfPet(dateTimeOfVisit);
                    usersRepository.save(user);
                }
            } else if (message.getText().startsWith("Рацион питания")) {
                Report report = new Report();
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                Pet pet = petRepository.findPetByUsers(user)
                        .orElseThrow(NotFoundException::new);
                report.setDataTime(LocalDateTime.now());
                report.setUser(user);
                report.setPet(pet);
                report.setDiet(message.getText());
                reportRepository.save(report);
                execute(SendMessage.builder()
                        .text("Опишите пожалуйста общее самочувствие питомца.\n" +
                                "Начните со слов Общее самочувствие.")
                        .chatId(message.getChatId()).build());
            } else if (message.getText().startsWith("Общее самочувствие")) {
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                List<Report> reports = reportRepository.findReportsByUser(user);
                Report report = new Report();
                for (Report r : reports) {
                    if (r.getDataTime().toLocalDate().equals(LocalDate.now())) {
                        report = r;
                    }
                }
                report.setStateOfHealth(message.getText());
                reportRepository.save(report);
                execute(SendMessage.builder()
                        .text("Опишите пожалуйста изменения в поведении питомца.\n" +
                                "Начните со слов Поведение питомца.")
                        .chatId(message.getChatId()).build());
            } else if (message.getText().startsWith("Поведение питомца")) {
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                List<Report> reports = reportRepository.findReportsByUser(user);
                Report report = new Report();
                for (Report r : reports) {
                    if (r.getDataTime().toLocalDate().equals(LocalDate.now())) {
                        report = r;
                    }
                }
                report.setHabits(message.getText());
                reportRepository.save(report);
                execute(SendMessage.builder()
                        .text("Загрузите пожалуйста актуальную фотографию питомца.")
                        .chatId(message.getChatId()).build());
            } else if (message.getText().matches("^((\\+7\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{2}[- .]?\\d{2}$")) {
>>>>>>>>> Temporary merge branch 2
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                user.setPhoneNumber(message.getText());
                usersRepository.save(user);
                execute(SendMessage.builder()
                        .text("Введите пожалуйста адрес Вашей электронной почты.")
                        .chatId(message.getChatId()).build());
<<<<<<<<< Temporary merge branch 1
            } else if (matcher4.matches()) {
=========
            } else if (message.getText().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
>>>>>>>>> Temporary merge branch 2
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                user.setEmailAddress(message.getText());
                usersRepository.save(user);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text("Спасибо. Выберете дальнейшее действие.")
                        .chatId(message.getChatId())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            } else {
                execute(SendMessage.builder()
                        .text("Вы ввели неверные данные. Повторите еще раз.")
                        .chatId(message.getChatId()).build());
            }
        } else if (message.hasPhoto()) {
            Users user = usersRepository.findUsersByChatId(message.getChatId())
                            .orElseThrow(NotFoundException::new);
            Pet pet = petRepository.findPetByUsers(user)
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
            FileInputStream is =  new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("photo.jpg",
                    "photo.jpg", "image/jpeg",
                    IOUtils.toByteArray(is));

            avatarService.upLoadAvatar(pet.getId(), multipartFile);

            List<Report> reports = reportRepository.findReportsByUser(user);
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
            avatarRepository.save(avatar);

            if (!(report.getDiet() == null && report.getStateOfHealth() == null
                    && report.getHabits() == null)) {
                execute(SendMessage.builder().text("Ваш отчет принят.")
                        .chatId(message.getChatId()).build());
            }
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException,
            IOException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Long petId = null;
        if (param.length > 1) {
            petId = Long.valueOf(param[1]);
        }
        switch (action) {

            case "INFORMATION" -> processingInfoMenu(message);

            case "TAKE" -> processingAdoptiveParentsMenu(message);

            case "REPORT" -> {
                execute(SendMessage.builder()
                        .text("Вам необходимо описать рацион питания, общее самочувствие, " +
                                "изменения в поведении и загрузить актуальную фотографию" +
                                "Вашего питомца.")
                        .chatId(message.getChatId().toString())
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
                execute(SendMessage.builder()
                        .text("Опишите пожалуйста рацион питания питомца.\n" +
                                "Начните со слов Рацион питания.")
                        .chatId(message.getChatId().toString())
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }

            case "CALL" -> execute(SendMessage.builder()
                    .text("Вызов волонтера")
                    .chatId(message.getChatId().toString())
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());

            case "WORKING" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
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

            case "SAFETY" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getSafetyPrecautions())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }

            case "CONTACT" -> execute(SendMessage.builder()
                    .text("Введите номер Вашего телефона в формате: \n" +
                            MessagesForUsers.FORMAT_PHONE_NUMBER)
                    .chatId(message.getChatId().toString())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(getStartButton()).build())
                    .build());

            case "ARRANGEMENT" -> processingArrangementMenu(message);

            case "CHOOSING" -> choosingOfPet(message);
            case "START" -> processingStartMenu(message);
            case "RULES" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getDatingRules())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "DOCUMENTS" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getListOfDocuments())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "TRANSPORTATION" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getAdviceForTransporting())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }

            case "ADVICE" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getTipsOfDogHandler())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "CYNOLOGIST" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getListOfDogHandler())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "REFUSAL" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getReasonsForRefusal())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }

            case "PUPPIES" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getAdviceForHomeForPuppy())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "DOG_ADULT" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getAdviceForHomeForAdultDog())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
            case "DOG_LIMITED" -> {
                Info info = infoService.getInfo(1L);
                List<List<InlineKeyboardButton>> buttons = getButtons();
                execute(SendMessage.builder()
                        .text(info.getAdviceForHomeForDogWithDisability())
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                        .build());
            }
//            case "KITTENS" -> {
//            }
//            case "CAT_ADULT" -> {
//            }
//            case "CAT_LIMITED" -> {
//            }

            case "TAKING" -> takingPet(message, petId);
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

    private static List<List<InlineKeyboardButton>> getButtons() {
        List<List<InlineKeyboardButton>> buttons = getStartButton();
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name())
                        .build()));
        return buttons;
    }

    private static List<List<InlineKeyboardButton>> getStartButton() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Перейти к стартовому меню")
                        .callbackData("START:")
                        .build()));
        return buttons;
    }

    private void takingPet(Message message, Long petId) throws TelegramApiException {
        Users user = usersRepository.findUsersByChatId(message.getChatId())
                .orElseThrow(NotFoundException::new);
        if (user.getPhoneNumber() == null) {
            List<List<InlineKeyboardButton>> button = new ArrayList<>();
            button.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(InfoMenuEnum.CONTACT.getInfo())
                            .callbackData(InfoMenuEnum.CONTACT.name())
                            .build()));
            execute(SendMessage.builder().text("Вам необходимо зарегистрироваться")
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().
                            keyboard(button).build())
                    .build());

        } else {
            Pet pet = petService.getPetByPetId(petId);
            if (pet != null) {
                pet.setUsers(user);
                user.setPet(pet);
                user.setRole(UserType.OWNER);
                usersRepository.save(user);
                petRepository.save(pet);

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
                execute(SendMessage.builder()
                        .text("Вы ввели неверные данные. Повторите еще раз.")
                        .chatId(message.getChatId()).build());
            }
        }
    }

    private void choosingOfPet(Message message) throws TelegramApiException,
            FileNotFoundException {
        List<Pet> allPetsByAnimalType = petService.getPetsByTypeOfAnimal(AnimalType.DOG);
        List<Pet> petList = allPetsByAnimalType.stream()
                .filter(e->e.getUsers() == null).toList();
        for (Pet pet : petList) {
            List<Avatar> avatars = avatarService.getAvatarsByPetId(pet.getId());

            File image = ResourceUtils.getFile(avatars.get(0).getFilePath());
            InputFile inputFile = new InputFile(image, image.getName());

            List<List<InlineKeyboardButton>> button = new ArrayList<>();
            button.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text("Выбрать питомца " + pet.getId())
                            .callbackData("TAKING:" + pet.getId())
                            .build())
            );

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
                        .keyboard(getStartButton()).build())
                .build());
    }

    private void processingArrangementMenu(Message message) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (ArrangementMenuEnum menu : ArrangementMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name())
                            .build())
            );
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name())
                        .build()));

        execute(SendMessage.builder()
                .text("Выбирите категорию животного")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingAdoptiveParentsMenu(Message message) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (AdoptiveParentsMenuEnum menu : AdoptiveParentsMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name())
                            .build())
            );
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(InfoMenuEnum.CONTACT.getInfo())
                        .callbackData(InfoMenuEnum.CONTACT.name())
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CALL.getInfo())
                        .callbackData(StartMenuEnum.CALL.name())
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name())
                        .build()));
        execute(SendMessage.builder()
                .text("Подбор животного")
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingInfoMenu(Message message) throws TelegramApiException {
        Info info = infoService.getInfo(1l);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (InfoMenuEnum menu : InfoMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name())
                            .build())
            );
        }
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CALL.getInfo())
                        .callbackData(StartMenuEnum.CALL.name())
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name())
                        .build()));
        execute(SendMessage.builder()
                .text(info.getAboutShelter())
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());
    }

    private void processingStartMenu(Message message) throws TelegramApiException {
        User caller = message.getFrom();
        if (usersRepository.findUsersByChatId(message.getChatId()).isEmpty()) {
            Users newUser = new Users();
            newUser.setUserName(caller.getFirstName());
            newUser.setChatId(message.getChatId());
            newUser.setRole(UserType.USER);
            usersRepository.save(newUser);
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (StartMenuEnum menu : StartMenuEnum.values()) {
            buttons.add(Collections.singletonList(
                    InlineKeyboardButton.builder()
                            .text(menu.getInfo())
                            .callbackData(menu.name())
                            .build())
            );
        }
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
            List<Report> reports = reportService.getReportsByUser(user);
            Report lastReport = reports.stream()
                    .max(Comparator.comparing(Report::getId))
                    .orElseThrow(NotFoundException::new);
            if ((lastReport == null) ||
                    (!(lastReport.getDataTime().toLocalDate().equals(LocalDate.now())                    )
                    && reports.size() < 30)) {
                execute(SendMessage.builder()
                        .text("Вы сегодня не прислали отчет о питомце. \n" +
                        "Настоятельно рекомендуем Вам срочно прислать отчет.")
                        .chatId(user.getChatId()).build());
            }
        }
    }
}
