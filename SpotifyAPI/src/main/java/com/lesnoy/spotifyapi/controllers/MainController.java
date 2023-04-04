package com.lesnoy.spotifyapi.controllers;

import com.lesnoy.spotifyapi.services.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MainController {

    private final SpotifyService spotifyService;

    @GetMapping("/track")
    public String getCurrentTrack(@RequestParam("username") String username) {
        return spotifyService.getCurrentTrack(username);
    }
}
