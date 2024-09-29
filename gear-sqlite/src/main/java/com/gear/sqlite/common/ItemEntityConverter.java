package com.gear.sqlite.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear.sqlite.db.ItemEntity;

import javax.persistence.AttributeConverter;

public class ItemEntityConverter implements AttributeConverter<ItemEntity, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ItemEntity itemEntity) {
        try {
            return objectMapper.writeValueAsString(itemEntity);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public ItemEntity convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, ItemEntity.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
