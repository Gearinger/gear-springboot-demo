package com.gear.gis.tool;

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
public class GearFeature {

    private final SimpleFeature simpleFeature;

    @Getter
    private final Geometry geometry;

    public GearFeature(SimpleFeature simpleFeature) {
        this.simpleFeature = simpleFeature;
        this.geometry = (Geometry) simpleFeature.getDefaultGeometry();
    }

    public CoordinateReferenceSystem getCrs() {
        return simpleFeature.getFeatureType().getCoordinateReferenceSystem();
    }

    public boolean setGeometry(Geometry geom) {
        simpleFeature.setDefaultGeometry(geom);
        return true;
    }

    public Boolean setFieldValue(String fieldName, Object value) {
        simpleFeature.setAttribute(fieldName, value);
        return true;
    }

    public String getId() {
        return this.simpleFeature.getID();
    }

    public Object getFieldValue(String fieldName) {
        if (StringUtils.hasLength(fieldName)) {
            return simpleFeature.getAttribute(fieldName);
        }
        return null;
    }

    public String toGeoJson() {
        return GeoJSONWriter.toGeoJSON(this.simpleFeature);
    }

}
