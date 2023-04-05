package com.lesnoy.spotifyapi.services;

import com.lesnoy.spotifyapi.config.TokenExpiredException;
import com.lesnoy.spotifyapi.config.UserNotAuthorizedException;
import com.lesnoy.spotifyapi.entity.Track;
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
        Track track = null;
        try {
            track = apiService.getCurrentTrack(username);
        } catch (TokenExpiredException e) {
            oAuthService.refreshToken(username);
            track = apiService.getCurrentTrack(username);
        } catch (UserNotAuthorizedException e) {
            return getRegistrationURL(username);
        }

        return MessageConverter.convertStringToLink(track.toString(), track.getTrackUrl());
    }

}
