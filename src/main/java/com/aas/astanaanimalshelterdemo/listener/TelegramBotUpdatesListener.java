package com.aas.astanaanimalshelterdemo.listener;

import com.aas.astanaanimalshelterdemo.botModel.*;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.AdoptiveParentsMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.ArrangementMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.InfoMenuEnum;
import com.aas.astanaanimalshelterdemo.botModel.buttonsMenu.StartMenuEnum;
import com.aas.astanaanimalshelterdemo.botRepositories.PetRepository;
import com.aas.astanaanimalshelterdemo.botRepositories.UsersRepository;
import com.aas.astanaanimalshelterdemo.botService.AvatarService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public TelegramBotUpdatesListener(InfoService infoService,
                                      PetService petService,
                                      AvatarService avatarService,
                                      UsersRepository usersRepository,
                                      PetRepository petRepository) {
        this.infoService = infoService;
        this.petService = petService;
        this.avatarService = avatarService;
        this.usersRepository = usersRepository;
        this.petRepository = petRepository;
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
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> first = message.getEntities()
                    .stream()
                    .filter(f -> "bot_command".equals(f.getType()))
                    .findFirst();
            if (first.isPresent()) {
                String command = message.getText()
                        .substring(first.get().getOffset(),
                                first.get().getLength());
                if ("/start".equals(command)) {
                    processingStartMenu(message);
                }
            }
        } else if (message.hasText()) {
            Pattern pattern1 = Pattern.compile("([0-9.:\\s]{16})");
            Matcher matcher1 = pattern1.matcher(message.getText());
            Pattern pattern2 = Pattern.compile("[0-9]");
            Matcher matcher2 = pattern2.matcher(message.getText());
            Pattern pattern3 = Pattern.compile("^(8|\\+7)(\\(\\d{3}\\))(\\d{3}-\\d{2}-\\d{2})$");
            Matcher matcher3 = pattern3.matcher(message.getText());
            Pattern pattern4 = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
            Matcher matcher4 = pattern4.matcher(message.getText());
            if (matcher1.matches()) {
                String dateTime = matcher1.group();
                LocalDateTime dateTimeOfVisit = parseDateTime(dateTime);
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
                            .chatId(message.getChatId()).build());
                    Users user = usersRepository.findUsersByChatId(message.getChatId())
                            .orElseThrow(NotFoundException::new);
                    user.setDataTimeOfPet(dateTimeOfVisit);
                    usersRepository.save(user);
                }
            } else if (matcher2.matches()){
                Long petId = Long.parseLong(message.getText());
                Pet pet = petService.getPetByPetId(petId);
                if (pet != null) {
                    takingPet(message);
                    Users user = usersRepository.findUsersByChatId(message.getChatId())
                            .orElseThrow(NotFoundException::new);
//                    usersRepository.findUsersByChatId(message.getChatId()).orElseThrow()
//                            .setPet(pet);
//                    petRepository.findById(petId).orElseThrow().setUsers(user);
                    pet.setUsers(user);
                    user.setPet(pet);
                    user.setRole(UserType.OWNER);
                    usersRepository.save(user);
                    petRepository.save(pet);
                }
            } else if (matcher3.matches()) {
                Users user = usersRepository.findUsersByChatId(message.getChatId())
                        .orElseThrow(NotFoundException::new);
                user.setPhoneNumber(message.getText());
                usersRepository.save(user);
                execute(SendMessage.builder()
                        .text("Введите пожалуйста адрес Вашей электронной почты.")
                        .chatId(message.getChatId()).build());
            } else if (matcher4.matches()) {
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
            }
        }
    }

    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException,
            IOException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        switch (action) {

            case "INFORMATION" -> processingInfoMenu(message);

            case "TAKE" -> processingAdoptiveParentsMenu(message);

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
//                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
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

//            case "TAKING.\*" -> takingPet(message);
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
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text("Перейти к стартовому меню")
                        .callbackData("START")
                        .build()));
        buttons.add(Collections.singletonList(
                InlineKeyboardButton.builder()
                        .text(StartMenuEnum.CHOOSING.getInfo())
                        .callbackData(StartMenuEnum.CHOOSING.name())
                        .build()));
        return buttons;
    }

    private void takingPet(Message message) throws TelegramApiException {
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
            Info info = infoService.getInfo(1L);
            execute(SendMessage.builder().text(MessagesForUsers.MESSAGE_FOR_NEW_TUTOR +
                            "\n" + info.getWorkMode() + "по адресу: " +
                            info.getAddress() +
                            "\n\n Введите пожалуйста дату и время, " +
                            "когда Вы сможете посетить наш приют, в формате: \n" +
                            MessagesForUsers.FORMAT_DATE_TIME)
                    .chatId(message.getChatId())
                    .build());
        }
    }

    private void choosingOfPet(Message message) throws TelegramApiException,
            FileNotFoundException {
        List<Pet> petList = petService.getPetsByTypeOfAnimal(AnimalType.DOG);
        for (Pet pet : petList) {
            List<Avatar> avatars = avatarService.getAvatarsByPetId(pet.getId());

            File image = ResourceUtils.getFile(avatars.get(0).getFilePath());
            InputFile inputFile = new InputFile(image, image.getName());

//            List<List<InlineKeyboardButton>> button = new ArrayList<>();
//            button.add(Collections.singletonList(
//                    InlineKeyboardButton.builder()
//                            .text("Выбрать этого питомца")
//                            .callbackData("TAKING" + ". idOfPet = " + pet.getId())
//                            .build())
//            );

            execute(SendMessage.builder()
                    .text("id: " + pet.getId() + "\nИмя: " + pet.getName()
                            + "\nВозраст: " + pet.getAge())
                    .chatId(message.getChatId().toString())
                    .build());

            execute(SendPhoto.builder()
                    .photo(inputFile)
                    .chatId(message.getChatId().toString())
//                    .replyMarkup(InlineKeyboardMarkup.builder().
//                            keyboard(button).build())
                    .build());

        }

        execute(SendMessage.builder()
                .text("Если Вы выбрали питомца, то введите его id.")
                .chatId(message.getChatId()).build());

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

}
