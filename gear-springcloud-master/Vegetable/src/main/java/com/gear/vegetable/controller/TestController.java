package com.gear.vegetable.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/vegetables")
public class TestController {

    @Value("${test.config}")
    private String testConfig;

    @RequestMapping("config")
    public String config(){
        return testConfig;
    }

    @RequestMapping("potato")
    public String potato(){
        return "potato";
    }
}
