package com.gear.gis.tool.interfaces.impl;

import com.gear.gis.tool.interfaces.IGearFeature;
import lombok.Getter;
import org.geotools.data.geojson.GeoJSONWriter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.util.StringUtils;


/**
 * 要素
 *
 * @author GuoYingdong
 * @date 2022/03/03
 */
public class GearFeature implements IGearFeature {

    private final SimpleFeature simpleFeature;

    @Getter
    private final Geometry geometry;

    public GearFeature(SimpleFeature simpleFeature) {
        this.simpleFeature = simpleFeature;
        this.geometry = (Geometry) simpleFeature.getDefaultGeometry();
    }

    @Override
    public CoordinateReferenceSystem getCrs() {
        return simpleFeature.getFeatureType().getCoordinateReferenceSystem();
    }

    @Override
    public boolean setGeometry(Geometry geom) {
        simpleFeature.setDefaultGeometry(geom);
        return true;
    }

    @Override
    public Boolean setFieldValue(String fieldName, Object value) {
        simpleFeature.setAttribute(fieldName, value);
        return true;
    }

    @Override
    public String getId() {
        return this.simpleFeature.getID();
    }

    @Override
    public Object getFieldValue(String fieldName) {
        if (StringUtils.hasLength(fieldName)) {
            return simpleFeature.getAttribute(fieldName);
        }
        return null;
    }

    @Override
    public String toGeoJson() {
        return GeoJSONWriter.toGeoJSON(this.simpleFeature);
    }

    @Override
    public String getGeometryByWkt() {
        WKTWriter writer = new WKTWriter();
        return writer.write(this.geometry);
    }

    @Override
    public byte[] getGeometryByWkb() {
        WKBWriter writer = new WKBWriter();
        return writer.write(this.geometry);
    }

    @Override
    public SimpleFeature setGeometryByWkt(String wkt) {
        return null;
    }

    @Override
    public SimpleFeature setGeometryByWkb(byte[] wkb) {
        return null;
    }

    @Override
    public SimpleFeature fromGeoJson(String geoJson) {
        return null;
    }
}
