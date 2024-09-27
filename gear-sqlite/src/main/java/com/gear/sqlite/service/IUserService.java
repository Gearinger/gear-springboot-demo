package com.gear.sqlite.service;

import com.gear.sqlite.db.UserEntity;

import java.util.List;

public interface IUserService {

    List<UserEntity> list();

    UserEntity save(UserEntity user);
}
