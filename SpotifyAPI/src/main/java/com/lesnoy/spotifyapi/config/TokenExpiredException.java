package com.lesnoy.spotifyapi.config;

public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
