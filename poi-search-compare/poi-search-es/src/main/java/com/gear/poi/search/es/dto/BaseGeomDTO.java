package com.gear.poi.search.es.dto;

import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
public class BaseGeomDTO {
    private Point location;
}
