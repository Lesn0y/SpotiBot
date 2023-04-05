package com.lesnoy.spotifyapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final SpotifyOAuthService oAuthService;
    private final SpotifyAPIService apiService;

    public void requestAccessToken(String code, String username) {
        oAuthService.requestAccessToken(code, username);
    }

    public String getRegistrationURL(String username) {
        return oAuthService.getRegistrationURL(username);
    }

    public String getCurrentTrack(String username) {
        String track = apiService.getCurrentTrack(username);
        if (track.equals("Token expired")) {
            oAuthService.refreshToken(username);
        }
        return apiService.getCurrentTrack(username);
    }

}
