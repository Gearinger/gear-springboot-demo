package com.gear.gis.tool.interfaces;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public interface IGearFeatureClass extends Iterable<IGearFeature> {

    /**
     * 获取要素总数
     *
     * @return {@link Integer}
     * @throws IOException ioexception
     */
    Integer getCount() throws IOException;

    /**
     * 获取要素源（可不使用）
     *
     * @return {@link SimpleFeatureSource}
     */
    SimpleFeatureCollection getFeatureCollection();

    /**
     * 坐标系
     * coordinate reference system
     *
     * @return {@link CoordinateReferenceSystem}
     */
    CoordinateReferenceSystem getCrs();

    /**
     * 得到边界
     *
     * @return {@link ReferencedEnvelope}
     * @throws IOException ioexception
     */
    ReferencedEnvelope getBounds() throws IOException;

    /**
     * 获取要素 stream (存在问题，无法使用)
     *
     * @return {@link Stream}<{@link IGearFeature}>
     */
    Stream<IGearFeature> getGearFeaturesStream();

    /**
     * 获取指定要素（遍历获取，效率低，尽量勿使用）
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return {@link IGearFeature}
     */
    IGearFeature getFeature(String fieldName, Object fieldValue);

    IGearFeature buildFeature(Geometry geometry, @Nullable Map<String, Object> fields);

    boolean addOneFeature(IGearFeature feature) throws IOException;

    boolean addFeatures(IGearFeatureClass featureClass) throws IOException;

    boolean addFeatures(List<IGearFeature> featureList) throws IOException;

    boolean addFeatures(IGearFeatureClass featureClass, Map<String, String> fieldNameMaps) throws IOException;

    /**
     * 修复
     *
     * @return {@link Boolean}
     */
    Boolean repair();

    List<String> getFieldNameList() throws IOException;

    String toGeoJson() throws IOException;

    /**
     * 相交
     *
     * @param featureClass 功能类
     * @return {@link IGearFeatureClass}
     */
    IGearFeatureClass intersect(IGearFeatureClass featureClass) throws ExecutionException, InterruptedException, IOException;

    /**
     * 擦除
     *
     * @param featureClass 功能类
     * @return {@link IGearFeatureClass}
     */
    IGearFeatureClass erase(IGearFeatureClass featureClass);

    String exportShp(String shpFile) throws Exception;

}
