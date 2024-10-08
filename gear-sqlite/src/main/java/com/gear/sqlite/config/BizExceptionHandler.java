package com.gear.sqlite.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@RequiredArgsConstructor
public class BizExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String exceptionHandler(Exception e) throws JsonProcessingException {
        System.out.printf("全局异常捕获>>>:%s%n", e);
        Result<Object> result = Result.error("系统异常");
        return objectMapper.writeValueAsString(result);
    }

}
