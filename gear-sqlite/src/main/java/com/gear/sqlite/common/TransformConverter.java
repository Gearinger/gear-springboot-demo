package com.gear.sqlite.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

@Slf4j
public class TransformConverter implements AttributeConverter<Transform, String> {

    @Override
    public String convertToDatabaseColumn(Transform transform) {
        return JSONObject.toJSONString(transform);
    }

    @Override
    public Transform convertToEntityAttribute(String s) {
        return JSONObject.parseObject(s, Transform.class);
    }
}
