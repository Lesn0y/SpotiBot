package com.lesnoy.spotifyapi.services;

import com.lesnoy.spotifyapi.entity.SpotifyToken;
import com.lesnoy.spotifyapi.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final SpotifyAuthService authService;
    private final SpotifyTrackService trackService;

    public String getRegistrationURL(String username) {
        return authService.getRegistrationURL(username);
    }

    public String getCurrentTrack(String username) {
        Track track = null;
        SpotifyToken token = authService.getUserToken(username);
        if (token != null) {
            track = trackService.getCurrentTrack(username, token);
            return MessageConverter.convertStringToSongLink(track);
        } else {
            return authService.getRegistrationURL(username);
        }
    }

    public void requestAccessToken(String code, String username) {
        authService.requestAccessToken(code, username);
    }
}
