package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.UserEntity;
import com.gear.sqlite.mapper.UserMapper;
import com.gear.sqlite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Override
    public List<UserEntity> list() {
        return userMapper.findAll();
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userMapper.save(user);
    }
}
