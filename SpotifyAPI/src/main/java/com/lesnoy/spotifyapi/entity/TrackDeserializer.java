package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

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
        String author = authorNode.elements().next().get("name").asText();
        String name = itemNod.get("name").asText();
        return new Track(author, name);
    }
}
