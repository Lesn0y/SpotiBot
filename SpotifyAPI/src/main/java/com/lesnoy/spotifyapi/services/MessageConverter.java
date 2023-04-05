package com.lesnoy.spotifyapi.services;

public class MessageConverter {

    public static String convertStringToLink(String track, String url) {
        return "<a href='"+ url + "'>" + track + "</a>";
    }

}
