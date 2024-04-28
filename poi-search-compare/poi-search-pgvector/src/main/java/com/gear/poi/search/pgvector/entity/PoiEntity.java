package com.gear.poi.search.pgvector.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

/**
 * 则实体
 *
 * @author guoyingdong
 * @date 2024/04/03
 */
@Data
@Entity
@Table(name = "poi")
public class PoiEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private String type;

    private String info;

    @Transient
    private Point location;

    @Transient
    private String keyWords;

    @Transient
    private String avgVectorFromWords;

}
