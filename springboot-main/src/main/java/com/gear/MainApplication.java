package com.gear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主要应用程序
 *
 * @author guoyingdong
 * @date 2024/01/19
 */
@RestController
@SpringBootApplication
public class MainApplication {

    @GetMapping("/main")
    public String test() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
