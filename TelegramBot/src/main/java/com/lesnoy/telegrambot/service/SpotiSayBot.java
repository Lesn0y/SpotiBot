package com.lesnoy.telegrambot.service;

import com.lesnoy.telegrambot.config.BotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SpotiSayBot extends TelegramLongPollingBot {

    private final Logger logger = LoggerFactory.getLogger(SpotiSayBot.class);

    private final BotConfig config;
    private final SpotifyAPIService spotifyAPIService;

    public SpotiSayBot(BotConfig config, SpotifyAPIService spotifyAPIService) {
        super(config.getBotToken());
        this.config = config;
        this.spotifyAPIService = spotifyAPIService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            logger.info("Request from '" + username + "', chat id - " + chatId);
            String response = switch (update.getMessage().getText()) {
                case "/start" -> spotifyAPIService.start(username);
                case "/track" -> spotifyAPIService.getCurrentTrack(username);
                default -> "Неизвестная команда";
            };

            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setText(response);

            logger.info("Response to '" + username + "', chat id - " + chatId + " '" + response + "'");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }
}