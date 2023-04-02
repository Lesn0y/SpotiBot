package com.lesnoy.telegrambot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.telegrambot.config.BotConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class SpotiSayBot extends TelegramLongPollingBot {

    private final BotConfig config;

    public SpotiSayBot(BotConfig config) {
        super(config.getBotToken());
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            long chatId = update.getMessage().getChatId();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            try {
                ObjectMapper mapper = new ObjectMapper();

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://localhost:8080/api/v1/track")
                        .build();

                Response response = client.newCall(request).execute();

                String responseMessage = response.body().string();

                sendMessage.setText(responseMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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