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
            String username = update.getMessage().getFrom().getUserName();
            System.out.println("FROM - " + username);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            if (update.getMessage().getText().equals("auth")) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://localhost:8080/auth/registration?username=" + username)
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseMessage = response.body().string();
                    System.out.println(responseMessage);
                    sendMessage.setText(responseMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (update.getMessage().getText().equals("track")) {
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://localhost:8080/api/v1/track?username=" + username)
                            .build();

                    Response response = client.newCall(request).execute();

                    String responseMessage = response.body().string();

                    sendMessage.setText(responseMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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