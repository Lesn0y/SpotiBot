package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonDeserialize(using = TrackDeserializer.class)
@AllArgsConstructor
@Getter
public class Track {
    private String author;
    private String name;

    @Override
    public String toString() {
        return author + " - " + name;
    }
}
