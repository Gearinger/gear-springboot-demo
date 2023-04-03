package com.gear.gis.tool.common;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import java.util.Collection;
import java.util.Iterator;

/**
 * GeometryUtils
 *
 * @author GuoYingdong
 * @date 2022/03/08
 */
public class GeometryUtils {
    /**
     * TODO
     * 修复几何（暂时只修复面几何）
     *
     * @param geom 几何学
     * @return {@link Geometry}
     */
    public static Geometry repair(Geometry geom) {
        if (geom instanceof Polygon) {
            if (geom.isValid()) {
                geom.normalize();
                return geom;
            }
            Polygonizer polygonizer = new Polygonizer();
            addPolygon((Polygon) geom, polygonizer);
            return toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
        } else if (geom instanceof MultiPolygon) {
            if (geom.isValid()) {
                geom.normalize();
                return geom;
            }
            Polygon[] polygons = new Polygon[geom.getNumGeometries()];
            for (int n = geom.getNumGeometries(); n-- > 0; ) {
                Polygonizer polygonizer = new Polygonizer();
                addPolygon((Polygon) geom.getGeometryN(n), polygonizer);
                Geometry geometry = toPolygonGeometry(polygonizer.getPolygons(), geom.getFactory());
                polygons[n] = (Polygon) geometry;
            }
            return geom.getFactory().createMultiPolygon(polygons);
        } else {
            if (geom.isValid()) {
                geom.normalize();
            }
            return geom;
        }
    }

    private static void addPolygon(Polygon polygon, Polygonizer polygonizer) {
        addLineString(polygon.getExteriorRing(), polygonizer);
        for (int n = polygon.getNumInteriorRing(); n-- > 0; ) {
            addLineString(polygon.getInteriorRingN(n), polygonizer);
        }
    }

    private static void addLineString(LineString lineString, Polygonizer polygonizer) {
        if (lineString instanceof LinearRing) {
            lineString = lineString.getFactory().createLineString(lineString.getCoordinateSequence());
        }
        Point point = lineString.getFactory().createPoint(lineString.getCoordinateN(0));
        Geometry toAdd = lineString.union(point);
        polygonizer.add(toAdd);
    }

    private static Geometry toPolygonGeometry(Collection<Polygon> polygons, GeometryFactory factory) {
        switch (polygons.size()) {
            case 0:
                return null;
            case 1:
                return polygons.iterator().next();
            default:
                Iterator<Polygon> iter = polygons.iterator();
                Geometry ret = iter.next();
                while (iter.hasNext()) {
                    ret = ret.difference(iter.next());
                }
                return ret;
        }
    }
}
