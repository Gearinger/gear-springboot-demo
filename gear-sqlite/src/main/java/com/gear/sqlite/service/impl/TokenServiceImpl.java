package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.TokenEntity;
import com.gear.sqlite.mapper.TokenMapper;
import com.gear.sqlite.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final TokenMapper tokenMapper;

    @Override
    public TokenEntity getByToken(String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        return tokenMapper.findOne(Example.of(tokenEntity)).orElse(null);
    }

    @Override
    public TokenEntity getByRefreshToken(String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setRefreshToken(refreshToken);
        return tokenMapper.findOne(Example.of(tokenEntity)).orElse(null);
    }

    @Override
    public TokenEntity save(TokenEntity tokenEntity) {
        return tokenMapper.save(tokenEntity);
    }

    @Override
    public void removeByToken(String token) {
        TokenEntity tokenEntity = this.getByToken(token);
        tokenMapper.delete(tokenEntity);
    }
}
