package com.gear.demo.controller;


import com.gear.common.result.ResultBody;
import com.gear.demo.entity.User;
import com.gear.demo.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GuoYingdong
 * @since 2022-02-18
 */
@RestController
@Api(tags = "user")
@RequestMapping("/demo/user")
public class UserController {
    @Autowired
    IUserService userService;

    @GetMapping("/list")
    public ResultBody<List<User>> list() {
        List<User> list = userService.list();
        return ResultBody.success(list);
    }
}
