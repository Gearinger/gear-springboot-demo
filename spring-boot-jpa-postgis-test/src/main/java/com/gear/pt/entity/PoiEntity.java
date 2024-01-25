package com.gear.pt.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_poi")
public class PoiEntity implements Serializable {
    @Id
    @Column(name = "id", length = 64, unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;
    //com.vividsolutions.jts.geom.Point
    @Column(name = "location", columnDefinition = "geometry(Point,4326)")
    private Point location;
}
