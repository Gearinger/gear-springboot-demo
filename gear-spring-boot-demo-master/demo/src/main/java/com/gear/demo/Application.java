package com.gear.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;


@ComponentScan({"com.gear.common.**", "com.gear.demo.**", "com.gear.minio.**"})
@MapperScan("com.gear.demo.mapper*")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        printSysUrl(context);
    }

    private static void printSysUrl(ConfigurableApplicationContext context) {
        Environment environment = context.getBean(Environment.class);
        String port = environment.getProperty("server.port");
//        System.out.println("系统 url：http://127.0.0.1:" + port);
        System.out.println("druid url: http://127.0.0.1:" + port + "/druid/login.html");
        System.out.println("swagger url: http://127.0.0.1:" + port + "/doc.html");
    }
}
