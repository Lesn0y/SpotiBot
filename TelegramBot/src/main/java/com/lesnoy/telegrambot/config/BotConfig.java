package com.lesnoy.telegrambot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BotConfig {

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.bot.username}")
    private String botUsername;

    public BotConfig() {
    }

    public BotConfig(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotUsername() {
        return botUsername;
    }
}
