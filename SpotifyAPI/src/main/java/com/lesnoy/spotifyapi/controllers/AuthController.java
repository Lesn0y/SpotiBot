package com.lesnoy.spotifyapi.controllers;

import com.lesnoy.spotifyapi.services.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${spotify.registration-url}")
    private String registrationUrl;

    private final OAuthService OAuthService;

    public AuthController(OAuthService OAuthService) {
        this.OAuthService = OAuthService;
    }

    @GetMapping("/success")
    public void successCode(@RequestParam("code") String code,
                            @RequestParam("state") String username) {
        OAuthService.requestAccessToken(code, username);
    }

    @GetMapping("/registration")
    @ResponseBody
    public String registration(@RequestParam("username") String username) {
        System.out.println("REGISTRATION - " + username);
        return registrationUrl + username;
    }
}
