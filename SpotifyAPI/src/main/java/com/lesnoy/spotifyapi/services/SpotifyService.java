package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.JwtToken;
import com.lesnoy.spotifyapi.entity.Track;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class SpotifyService {

    private final TokenRepository tokenRepository;

    public SpotifyService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String getCurrentTrack(String username) {
        Optional<JwtToken> token = tokenRepository.findById(username);
        if (token.isPresent()) {
            System.out.println("TOKEN - " + token);
            return spotifyTrackRequest(token.get().getAccessToken());
        } else {
            return null;
        }
    }

    private String spotifyTrackRequest(String accessToken) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player/currently-playing")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage = response.body().string();
            return objectMapper.readValue(responseMessage, Track.class).toString();
        } catch (IOException e) {
            System.out.println("RESPONSE EXCEPTION");
        }
        return "ERROR";
    }
}
