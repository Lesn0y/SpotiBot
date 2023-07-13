package com.lesnoy.spotifyapi.services;

import com.lesnoy.spotifyapi.entity.SpotifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<SpotifyToken, String> {
}
