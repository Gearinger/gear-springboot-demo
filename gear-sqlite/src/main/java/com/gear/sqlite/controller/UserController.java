package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
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
    public Result<List<UserEntity>> getAll() {
        List<UserEntity> list = userService.list();
        return Result.success(list);
    }

    @PostMapping("/add")
    public Result<UserEntity> add(UserEntity user) {
        UserEntity userEntity = userService.save(user);
        return Result.success(userEntity);
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        userService.delete(id);
    }
}
