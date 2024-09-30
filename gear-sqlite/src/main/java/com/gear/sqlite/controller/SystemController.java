package com.gear.sqlite.controller;

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
    public UserDTO login(String username, String password, HttpServletRequest request) {
        return userService.login(username, password, request);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserRegisterDTO user) {
        return userService.register(user);
    }

    @PostMapping("/refreshToken")
    public UserDTO refreshToken(String refreshToken) {
        return userService.refreshToken(refreshToken);
    }

}
