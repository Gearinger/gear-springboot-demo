package com.gear.sqlite.enums;

import lombok.Getter;

import java.util.Arrays;

public enum RoleEnum {
    ADMIN("管理员"),
    USER("普通用户"),
    ;

    @Getter
    private String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public static RoleEnum resolve(String s) {
        return Arrays.stream(RoleEnum.values()).filter(e -> e.name.equals(s)).findFirst().orElse(USER);
    }
}
