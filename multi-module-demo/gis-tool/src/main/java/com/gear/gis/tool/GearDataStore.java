package com.gear.gis.tool;

import lombok.Getter;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GearDataStore implements AutoCloseable {

    private final DataStore dataStore;
    @Getter
    GearConnection connect;
    /**
     * 默认要素类的名称（取第一个）
     */
    private String defaultFeatureClassName;

    public GearDataStore(GearConnection connect) throws Exception {
        this.connect = connect;
        switch (connect.getDbType()) {
            case ShpFile:
                File file = new File(connect.getShpFile());
                Map<String, Object> params = new HashMap<>();
                params.put("url", file.toURI().toURL());
                DataStore shpDataStore = DataStoreFinder.getDataStore(params);
                this.dataStore = shpDataStore;
                this.defaultFeatureClassName = shpDataStore.getTypeNames()[0];
                break;
            case PostGIS:
                params = new HashMap<>();
                params.put(PostgisNGDataStoreFactory.DBTYPE.key, connect.getDbType().getValue());
                params.put(PostgisNGDataStoreFactory.HOST.key, connect.getHost());
                params.put(PostgisNGDataStoreFactory.PORT.key, connect.getPort());
                //数据库名
                params.put(PostgisNGDataStoreFactory.DATABASE.key, connect.getDataBase());
                if (connect.getSchema()!=null){
                    params.put(PostgisNGDataStoreFactory.SCHEMA.key, connect.getSchema());
                }
                //用户名和密码
                params.put(PostgisNGDataStoreFactory.USER.key, connect.getUsername());
                params.put(PostgisNGDataStoreFactory.PASSWD.key, connect.getPasswd());
                this.dataStore = DataStoreFinder.getDataStore(params);
                break;
            default:
                throw new Exception("未实现该数据源的读取！");
        }
    }

    public GearFeatureClass readFeatureClass(String featureClassName) throws IOException {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureClassName);
        return new GearFeatureClass(featureSource);
    }

    public GearFeatureClass readFeatureClass() throws IOException {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(defaultFeatureClassName);
        return new GearFeatureClass(featureSource);
    }

    public String[] listFeatureClassName() throws IOException {
        return dataStore.getTypeNames();
    }

    public Boolean importFeatureClass(SimpleFeatureIterator sourceFeatureIterator, String targetFeatureClassName, Map<String, String> fieldNameMaps) {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend = dataStore.getFeatureWriterAppend(targetFeatureClassName, Transaction.AUTO_COMMIT)) {
            while (sourceFeatureIterator.hasNext()) {
                SimpleFeature sourceFeature = sourceFeatureIterator.next();
                SimpleFeature targetFeature = featureWriterAppend.next();
                transformFeature(sourceFeature, targetFeature, fieldNameMaps);
                featureWriterAppend.write();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean importFeatureClass(SimpleFeatureIterator sourceFeatureIterator, String targetFeatureClassName) {
        return this.importFeatureClass(sourceFeatureIterator, targetFeatureClassName, null);
    }

    @Override
    public void close() {
        this.dataStore.dispose();
    }

    private SimpleFeature transformFeature(SimpleFeature sourceFeature, SimpleFeature targetFeature, Map<String, String> fieldNameMaps) throws Exception {
        // 判断两者坐标系
        CoordinateReferenceSystem sourceCRS = sourceFeature.getFeatureType().getCoordinateReferenceSystem();
        CoordinateReferenceSystem targetCRS = targetFeature.getFeatureType().getCoordinateReferenceSystem();
        if (!sourceCRS.getName().getCode().equals(targetCRS.getName().getCode())) {
            throw new Exception("前后坐标系不一致，不能转换！");
        }

        Collection<Property> properties = sourceFeature.getProperties();
        for (Property property : properties) {
            String sourceFieldName = property.getName().toString();
            Object sourceFieldValue = property.getValue();
            String targetFieldName = fieldNameMaps == null ? sourceFieldName : fieldNameMaps.get(sourceFieldName);
            targetFeature.setAttribute(targetFieldName, sourceFieldValue);
            targetFeature.setDefaultGeometry(sourceFeature.getDefaultGeometry());
        }
        return targetFeature;
    }
}
