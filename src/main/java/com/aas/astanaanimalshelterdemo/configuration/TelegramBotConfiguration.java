package com.aas.astanaanimalshelterdemo.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TelegramBotConfiguration {

    @Value("5581462800:AAEtjTsp8-A8Vux-yB1utyhLQyW8WbOTfsg")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
