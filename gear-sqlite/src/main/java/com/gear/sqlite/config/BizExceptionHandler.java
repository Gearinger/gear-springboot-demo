package com.gear.sqlite.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class BizExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public String validateExceptionHandler(ConstraintViolationException e) throws JsonProcessingException {
        log.error("参数异常", e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .reduce("", (a, b) -> a + b);
        Result<Object> result = Result.error("参数异常:" + message);
        return objectMapper.writeValueAsString(result);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String exceptionHandler(Exception e) throws JsonProcessingException {
        log.error("系统异常", e);
        Result<Object> result = Result.error("系统异常:" + e.getMessage());
        return objectMapper.writeValueAsString(result);
    }

}
