package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.RefreshTokenResponse;
import com.lesnoy.spotifyapi.entity.SpotifyToken;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SpotifyOAuthService {

    private final Logger logger = LoggerFactory.getLogger(SpotifyOAuthService.class);

    @Value("${spotify.client-id}")
    private String clientId;
    @Value("${spotify.client-secret}")
    private String clientSecret;
    @Value("${spotify.redirect-url}")
    private String redirectUrl;
    @Value("${spotify.registration-url}")
    private String registrationUrl;

    private final TokenRepository tokenRepository;

    public void requestAccessToken(String code, String username) {
        SpotifyToken token = authorizationRequest(code, username);
        if (token != null) {
            tokenRepository.save(token);
            logger.info("Save user '" + username + "' with token '" + token + "'");
        } else {
            logger.info("User '" + username + " has not been saved");
        }
    }

    public void refreshToken(String username) {
        OkHttpClient client = new OkHttpClient();

        SpotifyToken token = tokenRepository.findById(username).get();

        logger.info("Old " + username + " access token - " + token.getAccessToken());

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", token.getRefreshToken())
                .build();

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .header("Authorization", getAuthorizationHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage;
            if (response.body() != null) {
                responseMessage = response.body().string();
                var tokenResponse = objectMapper.readValue(responseMessage, RefreshTokenResponse.class);
                token.setAccessToken(tokenResponse.getAccessToken());
                tokenRepository.save(token);
                logger.info("Newest " + username + " access token - " + token.getAccessToken());
            } else {
                logger.info(username + " token not updated");
            }
        } catch (IOException e) {
            logger.error("Spotify API 'Get Currently Playing Track' error");
            e.printStackTrace();
        }
    }

    public String getRegistrationURL(String username) {
        return registrationUrl + "?" +
                "client_id=" + clientId + "&" +
                "response_type=code&" +
                "redirect_uri=" + redirectUrl + "&" +
                "scope=user-read-playback-state&" +
                "state=" + username;
    }

    private SpotifyToken authorizationRequest(String code, String username) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token?" +
                        "grant_type=authorization_code&" +
                        "redirect_uri=" + redirectUrl + "&" +
                        "code=" + code)
                .header("Authorization", getAuthorizationHeader())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(RequestBody.create(new byte[0]))
                .build();

        logger.info("Send request to " + request.url() + ", to get token of " + username);

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseText = responseBody.string();
                logger.info("Response for " + username + " - '" + responseText + "'");
                SpotifyToken spotifyToken = objectMapper.readValue(responseText, SpotifyToken.class);
                spotifyToken.setUsername(username);
                return spotifyToken;
            }
        } catch (IOException e) {
            logger.warn("Error on receiving an authorization response for " + username);
            e.printStackTrace();
        }
        return null;
    }

    private String getAuthorizationHeader() {
        String value = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(value.getBytes());
    }
}
