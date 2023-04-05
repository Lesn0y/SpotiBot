package com.lesnoy.spotifyapi.services;

import com.lesnoy.spotifyapi.entity.SpotifyToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<SpotifyToken, String> {
}
