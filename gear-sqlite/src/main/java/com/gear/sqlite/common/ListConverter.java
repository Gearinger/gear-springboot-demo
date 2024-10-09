package com.gear.sqlite.common;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ListConverter implements AttributeConverter<List, String> {

    @Override
    public String convertToDatabaseColumn(List list) {
        return JSONObject.toJSONString(list);
    }

    @Override
    public List convertToEntityAttribute(String s) {
        return JSONObject.parseObject(s, List.class);
    }
}
