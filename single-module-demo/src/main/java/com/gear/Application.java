package com.gear;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@SpringBootApplication
public class Application {

    @Autowired
    Config config;

    @Autowired
    Config2 config2;

    @GetMapping("/")
    public String home() {
        StringBuilder builder = new StringBuilder();
        System.out.println(config.toString());
        builder.append(config.toString());

        System.out.println(config2.toString());
        builder.append(config2.toString());

        return builder.toString();
    }


    public static void main(String[] args) {
        System.out.println(new File("").getAbsolutePath());
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        printSysUrl(context);
    }

    private static void printSysUrl(ConfigurableApplicationContext context) {

        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("local.server.port");
        System.out.println("系统 url：http://127.0.0.1:" + port);
        System.out.println("druid url: http://127.0.0.1:" + port + "/druid/login.html");
        System.out.println("swagger url: http://127.0.0.1:" + port + "/doc.html");
    }
}
