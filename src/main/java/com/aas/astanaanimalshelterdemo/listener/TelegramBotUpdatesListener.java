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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public TelegramBotUpdatesListener(InfoService infoService,
                                      PetService petService,
                                      AvatarService avatarService,
                                      DogUsersService dogUsersService,
                                      CatUsersService catUsersService) {
        this.infoService = infoService;
        this.petService = petService;
        this.avatarService = avatarService;
        this.dogUsersService = dogUsersService;
        this.catUsersService = catUsersService;
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
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        AnimalType type = checkedType(message);
        if (message.hasText()) {
            if ("/start".equals(message.getText())) {
                chooseMenu(message, type);
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
        }
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
            case "REPORT" -> execute(SendMessage.builder()
                    .text("Отчет о животном")
                    .chatId(message.getChatId().toString())
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
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
            takingDog(message, type, petId);
        } else {
            takingCat(message, type, petId);
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
            dogUsersService.getUserByChatId(message.getChatId()).ifPresent(dogUsersService::delete);
        } else {
            catUsersService.getUserByChatId(message.getChatId()).ifPresent(catUsersService::delete);
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
}
