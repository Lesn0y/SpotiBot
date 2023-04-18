package com.lesnoy.spotifyapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tokens", schema = "spoti_bot")
public class SpotifyToken implements Serializable {
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
    @Column(name = "expiration_time")
    private Date expirationTime;
}

