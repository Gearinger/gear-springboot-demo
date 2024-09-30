package com.gear.sqlite.service;

import com.gear.sqlite.db.UserEntity;
import com.gear.sqlite.dto.UserDTO;
import com.gear.sqlite.dto.UserRegisterDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserService {

    List<UserEntity> list();

    UserEntity save(UserEntity user);

    UserDTO getUserByToken(String token);

    UserDTO login(String username, String password, HttpServletRequest request);

    UserDTO refreshToken(String refreshToken);

    void logout();

    UserEntity register(UserRegisterDTO user);
}
