package com.gear.mybatis.demo.controller;

import com.gear.mybatis.demo.entity.User;
import com.gear.mybatis.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    //查询用户列表
    @GetMapping("/queryUserList")
    public List<User> queryUserList(){
        List<User> userList = userMapper.queryUserList();
        for(User user:userList){
            System.out.println(user);
        }
        return userList;
    }
    //增加一个用户
    @GetMapping("/addUser")
    public String addUser(){
        userMapper.addUser(new User("小三"));
        return  "ok";
    }
    //修改一个用户
    @GetMapping("/updateUser")
    public String updateUser(){
        userMapper.updateUser(new User("小王"));
        return "ok";
    }
    //删除一个用户
    @GetMapping("deleteUser")
    public String deleteUser(){
        userMapper.deleteUser(5);
        return "ok";
    }
    @GetMapping("queryUserById")
    public User queryUserById(){
        User user = userMapper.queryUserById(2);
        System.out.println(user);
        return user;
    }

}
