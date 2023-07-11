package com.lesnoy.spotifyapi.services;

import com.lesnoy.spotifyapi.entity.Track;

public class MessageConverter {
    public static String convertStringToSongLink(Track track) {
        return "<a href='"+ track.getTrackUrl() + "'>" + track + "</a>";
    }

    public static String convertStringToRegUrl(String regUrl) {
        return "<a href='"+ regUrl + "'>" + "Нажми для авторизации" + "</a>";
    }
}
