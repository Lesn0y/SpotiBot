package com.lesnoy.spotifyapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesnoy.spotifyapi.entity.SpotifyToken;
import com.lesnoy.spotifyapi.entity.Track;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SpotifyTrackService {

    public Track getCurrentTrack(String username, SpotifyToken token) {
        log.info("Request the Spotify API to retrieve a '" + username + "' current track");
        return spotifyTrackRequest(token);
    }

    private Track spotifyTrackRequest(SpotifyToken token) {

        OkHttpClient client = new OkHttpClient();
        log.info("REQUEST TOKEN - " + token.getUsername() + " - " + token.getAccessToken());
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player/currently-playing")
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build();

        try (Response response = client.newCall(request).execute()) {

            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage;
            if (response.body() != null) {
                responseMessage = response.body().string();
                Track track = objectMapper.readValue(responseMessage, Track.class);
                log.info(token.getUsername() + " response from Spotify API - '" + track.toString() + "'");
                return track;
            }
        } catch (IOException e) {
            log.error("Spotify API 'Get Currently Playing Track' error");
            e.printStackTrace();
        }
        return null;
    }
}
