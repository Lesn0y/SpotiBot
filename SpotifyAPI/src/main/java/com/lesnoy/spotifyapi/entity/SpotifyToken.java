package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SpotifyToken {
    @Id
    @JsonProperty("username")
    private String username;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("expires_in")
    private Integer expires;
    @JsonProperty("refresh_token")
    private String refreshToken;
}

