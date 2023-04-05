package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@JsonDeserialize(using = TrackDeserializer.class)
@AllArgsConstructor
@Getter
public class Track {
    private List<String> author;
    private String name;
    private String trackUrl;

    @Override
    public String toString() {
        if (author.size() > 1) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < author.size() - 1; i++) {
                result.append(author.get(i)).append(", ");
            }
            result.append(author.get(author.size() - 1)).append(" - ").append(name);
            return result.toString();
        }
        return author.get(0) + " - " + name;
    }
}
