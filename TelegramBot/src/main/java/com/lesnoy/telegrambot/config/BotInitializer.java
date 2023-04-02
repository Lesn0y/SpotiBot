package com.lesnoy.telegrambot.config;

import com.lesnoy.telegrambot.service.SpotiSayBot;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private final SpotiSayBot spotiSayBot;

    public BotInitializer(SpotiSayBot spotiSayBot) {
        this.spotiSayBot = spotiSayBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(spotiSayBot);
    }
}
