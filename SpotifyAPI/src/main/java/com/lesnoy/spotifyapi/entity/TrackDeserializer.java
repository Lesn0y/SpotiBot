package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackDeserializer extends StdDeserializer<Track> {

    public TrackDeserializer() {
        this(null);
    }

    public TrackDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Track deserialize(JsonParser jp,
                             DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode jsonNode = jp.getCodec().readTree(jp);
        JsonNode itemNod = jsonNode.get("item");
        JsonNode authorNode = itemNod.get("artists");
        String trackUrl = itemNod.get("external_urls").get("spotify").asText();

        Iterator<JsonNode> authorsIt = authorNode.elements();
        List<String> authorsList = new ArrayList<>();
        while (authorsIt.hasNext()) {
            authorsList.add(authorsIt.next().get("name").asText());
        }

        String name = itemNod.get("name").asText();
        return new Track(authorsList, name, trackUrl);
    }
}
