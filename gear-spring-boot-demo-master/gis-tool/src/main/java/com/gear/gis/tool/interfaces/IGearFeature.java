package com.gear.gis.tool.interfaces;

import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public interface IGearFeature {

    /**
     * 获取坐标系名称
     *
     * @return {@link String}
     */
    CoordinateReferenceSystem getCrs();

    /**
     * geometry
     *
     * @return {@link Geometry}
     */
    Geometry getGeometry();

    boolean setGeometry(Geometry geom);

    /**
     * 设置字段值
     *
     * @param fieldName 字段名
     * @param value     价值
     * @return {@link Boolean}
     */
    Boolean setFieldValue(String fieldName, Object value);

    /**
     * 得到id
     *
     * @return {@link String}
     */
    String getId();

    /**
     * 获取字段值
     *
     * @param fieldName 字段名
     * @return {@link Object}
     */
    Object getFieldValue(String fieldName);

    /**
     * geojson
     *
     * @return {@link String}
     */
    String toGeoJson();

    /**
     * wkt
     *
     * @return {@link String}
     */
    String getGeometryByWkt();

    /**
     * wkb
     *
     * @return {@link String}
     */
    byte[] getGeometryByWkb();

    SimpleFeature fromGeoJson(String geoJson);

    SimpleFeature setGeometryByWkt(String wkt);

    SimpleFeature setGeometryByWkb(byte[] wkb);
}
