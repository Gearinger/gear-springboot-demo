package com.gear.sqlinit.domain;

import org.locationtech.jts.geom.MultiPolygon;

import javax.persistence.*;

@Entity
@Table(name = "region")
public class regionDB {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "geometry(MultiPolygon, 4326)")
    private MultiPolygon geom;

    /**
     * 行政区代码
     */
    private Integer pacCode;

    /**
     * 行政区名称
     */
    private String pacName;

    /**
     * 父级行政区代码
     */
    private Integer parentPacCode;
}
