package com.lesnoy.spotifyapi.controllers;

import com.lesnoy.spotifyapi.services.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Value("${spotify.registration-url}")
    private String registrationUrl;

    private final OAuthService authService;

    public AuthController(OAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/success")
    public void successCode(@RequestParam("code") String code,
                            @RequestParam("state") String username) {
        authService.requestAccessToken(code, username);
    }

    @GetMapping("/registration")
    @ResponseBody
    public String registration(@RequestParam("username") String username) {
        return registrationUrl + username;
    }
}
