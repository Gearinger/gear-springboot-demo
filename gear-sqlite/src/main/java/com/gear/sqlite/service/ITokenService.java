package com.gear.sqlite.service;

import com.gear.sqlite.db.TokenEntity;

public interface ITokenService {
    TokenEntity getByToken(String token);

    TokenEntity getByRefreshToken(String refreshToken);

    TokenEntity save(TokenEntity tokenEntity);

    void removeByToken(String token);
}
