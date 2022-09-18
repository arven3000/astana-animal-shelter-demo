package com.aas.astanaanimalshelterdemo.listener;

import com.aas.astanaanimalshelterdemo.botModel.Info;
import com.aas.astanaanimalshelterdemo.botService.InfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
public class TelegramBotUpdatesListener extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    private Message messageOfUser;
    private Message messageOfBot;

    private final InfoService infoService;

    public TelegramBotUpdatesListener(InfoService infoService) {
        this.infoService = infoService;
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
        if (update.hasMessage()) {
            messageOfUser = update.getMessage();
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            messageOfBot = update.getMessage();
            try {
                handleCallBack(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Обработка команд.
     */
    public void handleMessage(Message message) throws TelegramApiException {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".
                            equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText()
                        .substring(commandEntity.get().getOffset(),
                                commandEntity.get().getLength());
                switch (command) {
                    case "/start": processingCommandStart(message);
                }
            }
        }
    }

    /**
     * Обработка команды /start
     * @param message
     * @throws TelegramApiException
     */
    private void processingCommandStart(Message message) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Узнать информацию о приюте")
                                .callbackData("Узнать информацию о приюте")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Как взять собаку из приюта")
                                .callbackData("Как взять собаку из приюта")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Прислать отчет")
                                .callbackData("Прислать отчет")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Позвать волонтера")
                                .callbackData("Позвать волонтера")
                                .build()
                )
        );

        execute(
                SendMessage.builder()
                        .text("Здравствуйте, " + message.getFrom().getFirstName() + "! \n"
                        + "Вас приветствует телеграм-бот приюта Счастливый питомец. \n"
                        + "Выберете пожалуйста необходимое действие.")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboard(buttons)
                                .build())
                        .build()
        );
    }


    private void handleCallBack(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String button = callbackQuery.getData();
        switch (button) {
            case "Узнать информацию о приюте" : processingButtonInfo(message);
        }
    }

    private void processingButtonInfo(Message message) throws TelegramApiException {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Узнать режим работы и адрес приюта")
                                .callbackData("Узнать режим работы и адрес приюта")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Узнать правила поведения в приюте")
                                .callbackData("Узнать правила поведения в приюте")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Ввести контактные данные")
                                .callbackData("Ввести контактные данные")
                                .build()
                )
        );
        buttons.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Позвать волонтера")
                                .callbackData("Позвать волонтера")
                                .build()
                )
        );
        String aboutShelter = infoService.getInfo(2L).getAboutShelter();
        execute(
                SendMessage.builder()
                        .text("Уважаемый, " + messageOfUser.getFrom().getFirstName()
                                + "! \n\n" + aboutShelter +
                                "\n\n Выберете пожалуйста дальнейшее действие.")
                        .chatId(message.getChatId().toString())
                        .replyMarkup(InlineKeyboardMarkup.builder()
                                .keyboard(buttons)
                                .build())
                        .build()
        );
    }

}
