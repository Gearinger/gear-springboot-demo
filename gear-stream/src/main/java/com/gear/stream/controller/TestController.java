package com.gear.stream.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class TestController {

    @GetMapping("/outputStream")
    public void outputStream(HttpServletResponse response) throws IOException, InterruptedException {

        ServletOutputStream outputStream = response.getOutputStream();

        outputStream.write("hello world".getBytes());

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10);
            outputStream.write(String.valueOf(i).getBytes());
            outputStream.flush();
        }

    }

    @GetMapping("/asyncOutputStream")
    public void asyncOutputStream(HttpServletResponse response) throws IOException, InterruptedException {

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write("hello world".getBytes());

            Thread t = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(10);
                        outputStream.write(String.valueOf(i).getBytes());
                        outputStream.flush();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            t.start();
        }
    }

    @GetMapping("/asyncOutputStream2")
    public String asyncOutputStream2(HttpServletResponse response) throws IOException, InterruptedException {

        return "hello world";
    }


    @GetMapping("/text")
    public void text(HttpServletResponse response) throws IOException, InterruptedException {
        response.getWriter().write("hello world");
    }
}
