package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.SpotifyToken;
import com.lesnoy.spotifyapi.entity.Track;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpotifyAPIService {

    private final Logger logger = LoggerFactory.getLogger(SpotifyAPIService.class);

    private final TokenRepository tokenRepository;

    public String getCurrentTrack(String username) {
        Optional<SpotifyToken> token = tokenRepository.findById(username);
        if (token.isPresent()) {
            logger.info("Request the Spotify API to retrieve a '" + username + "' current track");
            return spotifyTrackRequest(token.get());
        } else {
            logger.info("User '" + username + "' has not been authorized before");
            return "Unauthorized";
        }
    }

    private String spotifyTrackRequest(SpotifyToken token) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player/currently-playing")
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) {
                logger.info("Response code for " + token.getUsername() + " - " + response.code());
                return "Token expired";
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseMessage;
                if (response.body() != null) {
                    responseMessage = response.body().string();
                    Track track = objectMapper.readValue(responseMessage, Track.class);
                    logger.info(token.getUsername() + " response from Spotify API - '" + track.toString() + "'");
                    return track.toString();
                }
            }
        } catch (IOException e) {
            logger.error("Spotify API 'Get Currently Playing Track' error");
            e.printStackTrace();
        }
        return "Not Found";
    }

}
