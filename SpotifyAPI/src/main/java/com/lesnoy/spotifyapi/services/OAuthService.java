package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.JwtToken;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class OAuthService {

    @Value("${spotify.client-id}")
    private String clientId;
    @Value("${spotify.client-secret}")
    private String clientSecret;

    private final TokenRepository tokenRepository;

    public OAuthService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void requestAccessToken(String code, String username) {
        JwtToken token = authorizationRequest(code);
        if (token != null) {
            System.out.println("USERNAME " + username);
            token.setUsername(username);
            tokenRepository.save(token);
        }
    }

    private JwtToken authorizationRequest(String code) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token?" +
                        "grant_type=authorization_code&" +
                        "redirect_uri=http://localhost:8080/auth/success&" +
                        "code=" + code)
                .header("Authorization", getAuthorizationValue())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(RequestBody.create(new byte[0]))
                .build();

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();

            return objectMapper.readValue(responseBody.string(), JwtToken.class);

        } catch (IOException e) {
            System.out.println("RESPONSE EXCEPTION");
        }
        return null;
    }

    private String getAuthorizationValue() {
        String value = clientId + ":" + clientSecret;
        return "Basic " + Base64.getEncoder().encodeToString(value.getBytes());
    }
}
