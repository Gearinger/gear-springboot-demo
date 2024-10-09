package com.gear.sqlite.common;

import com.gear.sqlite.enums.RoleEnum;

import javax.persistence.AttributeConverter;

public class RoleEnumConverter implements AttributeConverter<RoleEnum, String> {
    @Override
    public String convertToDatabaseColumn(RoleEnum t) {
        return t.getName();
    }

    @Override
    public RoleEnum convertToEntityAttribute(String s) {
        return RoleEnum.resolve(s);
    }
}
