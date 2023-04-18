package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.RefreshTokenResponse;
import com.lesnoy.spotifyapi.entity.SpotifyToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpotifyAuthService {

    @Value("${spotify.client-id}")
    private String clientId;
    @Value("${spotify.client-secret}")
    private String clientSecret;
    @Value("${spotify.redirect-url}")
    private String redirectUrl;
    @Value("${spotify.registration-url}")
    private String registrationUrl;

    private final TokenRepository tokenRepository;

    public String getRegistrationURL(String username) {
        return registrationUrl + "?" +
                "client_id=" + clientId + "&" +
                "response_type=code&" +
                "redirect_uri=" + redirectUrl + "&" +
                "scope=user-read-playback-state&" +
                "state=" + username;
    }

    @Cacheable("tokens")
    public SpotifyToken getUserToken(String username) {
        log.info("Request to db, find '" + username + "' access key");
        Optional<SpotifyToken> token = tokenRepository.findById(username);
        if (token.isPresent() && isTokenValid(token.get())) {
            return token.get();
        } else {
            return refreshToken(username);
        }
    }

    public void requestAccessToken(String code, String username) {
        SpotifyToken token = authorizationRequest(code, username);
        if (token != null) {
            log.info("Save user '" + username + "' with token '" + token + "'");
            tokenRepository.save(token);
        } else {
            log.info("User '" + username + " has not been saved");
        }
    }

    @CacheEvict(value = "tokens", allEntries = true)
    public SpotifyToken refreshToken(String username) {
        OkHttpClient client = new OkHttpClient();

        SpotifyToken token = tokenRepository.findById(username).get();

        log.info("Old " + username + " access token - " + token.getAccessToken());

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
                token.setExpirationTime(new Date(System.currentTimeMillis() + token.getExpires() * 1000));
                return tokenRepository.save(token);
            } else {
                log.info(username + " token not updated");
            }
        } catch (IOException e) {
            log.error("Spotify API 'Get Currently Playing Track' error");
            e.printStackTrace();
        }
        return null;
    }

    private boolean isTokenValid(SpotifyToken token) {
        return token.getExpirationTime().after(new Date());
    }

    @CacheEvict(value = "tokens", allEntries = true)
    public SpotifyToken authorizationRequest(String code, String username) {
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

        log.info("Send request to " + request.url() + ", to get token of " + username);

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseText = responseBody.string();
                log.info("Response for " + username + " - '" + responseText + "'");
                SpotifyToken token = objectMapper.readValue(responseText, SpotifyToken.class);
                token.setUsername(username);
                token.setExpirationTime(new Date(System.currentTimeMillis() + token.getExpires() * 1000));
                return token;
            }
        } catch (IOException e) {
            log.warn("Error on receiving an authorization response for " + username);
            e.printStackTrace();
        }
        return null;
    }

    private String getAuthorizationHeader() {
        String value = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(value.getBytes());
    }
}
