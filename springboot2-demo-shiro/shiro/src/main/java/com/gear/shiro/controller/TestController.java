package com.gear.shiro.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Api
public class TestController {

    @GetMapping("/login")
    public String logon(){
        return "~~~login~~~";
    }

    @GetMapping("/test1")
    public String test1(){
        return "~~~~~~~";
    }
}
