package com.lesnoy.spotifyapi.config;

public class UserNotAuthorizedException extends RuntimeException{

    public UserNotAuthorizedException() {
        super();
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
