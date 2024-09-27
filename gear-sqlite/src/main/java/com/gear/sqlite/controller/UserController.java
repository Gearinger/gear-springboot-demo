package com.gear.sqlite.controller;

import com.gear.sqlite.db.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gear.sqlite.service.IUserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    @GetMapping("/getAll")
    public List<UserEntity> getAll() {
        return userService.list();
    }

    @PostMapping("/add")
    public UserEntity add(UserEntity user) {
        return userService.save(user);
    }
}
