package com.gear;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear.config.SubmoduleConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要应用程序
 *
 * @author guoyingdong
 * @date 2024/01/19
 */
@RestController
@SpringBootApplication
public class MainApplication {

    @Autowired
    private SubmoduleConfig submoduleConfig;

    @GetMapping("/main")
    public String test() {
        return "Main: Hello World!";
    }

    @GetMapping("/config")
    public List<Object> config() throws JsonProcessingException {
        List<Object> list = new ArrayList<>();
        SubmoduleConfig submoduleConfig1 = new SubmoduleConfig();
        BeanUtils.copyProperties(submoduleConfig, submoduleConfig1);
        list.add(submoduleConfig1);
        return list;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
