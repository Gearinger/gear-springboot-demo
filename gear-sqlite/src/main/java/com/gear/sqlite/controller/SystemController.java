package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
import com.gear.sqlite.db.UserEntity;
import com.gear.sqlite.dto.UserDTO;
import com.gear.sqlite.dto.UserRegisterDTO;
import com.gear.sqlite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/system")
public class SystemController {

    private final IUserService userService;

    @PostMapping("/login")
    public Result<UserDTO> login(String username, String password, HttpServletRequest request) {
        UserDTO login = userService.login(username, password, request);
        return Result.success(login);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }

    @PostMapping("/register")
    public Result<UserEntity> register(@RequestBody UserRegisterDTO user) {
        UserEntity register = userService.register(user);
        return Result.success(register);
    }

    @PostMapping("/refreshToken")
    public Result<UserDTO> refreshToken(String refreshToken) {
        UserDTO userDTO = userService.refreshToken(refreshToken);
        return Result.success(userDTO);
    }

}
