package com.gear.sqlite.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

@Slf4j
public class TransformConverter implements AttributeConverter<Transform, String> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Transform transform) {
        try {
            return objectMapper.writeValueAsString(transform);
        } catch (JsonProcessingException e) {
            log.warn("Transform字段转换为字符串失败, {}", transform);
            return null;
        }
    }

    @Override
    public Transform convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, Transform.class);
        } catch (JsonProcessingException e) {
            log.warn("字符串转化为Transform字段失败, {}", s);
            return null;
        }
    }
}
