package com.gear.gis.tool.enums;

import lombok.Getter;

public enum DbType {
    /**
     * shp文件
     */
    ShpFile("ShpFile"),
    /**
     * postgis
     */
    PostGIS("postgis"),
    /**
     * 其他
     */
    Others("others");

    @Getter
    private final String value;

    DbType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
