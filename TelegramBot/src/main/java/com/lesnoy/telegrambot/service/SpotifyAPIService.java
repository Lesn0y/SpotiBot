package com.lesnoy.telegrambot.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpotifyAPIService {

    private final Logger logger = LoggerFactory.getLogger(SpotifyAPIService.class);

    public String start(String username) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://localhost:8080/auth/registration?username=" + username)
                    .build();

            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentTrack(String username) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/v1/track?username=" + username)
                    .build();

            Response response = client.newCall(request).execute();

            String track = response.body().string();

            if (track.equals("Unauthorized")) {
                logger.info("User '" + username + "' is not authorized");
                return "Пользователь не авторизован. <a href='"+ start(username) + "'>Пройти авторизацию</a>";
            }

            return track;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
