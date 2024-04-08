package com.gear.poi.search.es.dto;

import lombok.Data;

@Data
public class PoiDTO {
    private String id;

    private String name;

    private String address;

    private String type;

    private String info;

    /**
     * wkt point
     */
    private String location;
}
