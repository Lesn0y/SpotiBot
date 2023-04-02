package com.lesnoy.spotifyapi.controllers;

import com.lesnoy.spotifyapi.services.SpotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    private final SpotifyService spotifyService;

    public MainController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/track")
    public String getCurrentTrack(@RequestParam("username") String username) {
        System.out.println("TRACK FROM USERNAME - " + username);
        String track = spotifyService.getCurrentTrack(username);
        return track;
    }
}
