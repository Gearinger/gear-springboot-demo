package com.gear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear.config.SubmoduleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author guoyingdong
 * @date 2024/02/01
 */
@RestController
public class TestController {

    @Autowired
    SubmoduleConfig submoduleConfig;

    @GetMapping("/sub1")
    public String test() {
        return "Sub1: Hello World!";
    }

    @GetMapping("/sub1config")
    public String sub1config() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(submoduleConfig);
    }

}
