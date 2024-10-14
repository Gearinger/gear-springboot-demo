package com.gear.gis.tool;

import com.gear.gis.tool.util.FeatureClassUtil;
import com.gear.gis.tool.util.GeometryUtils;
import com.gear.gis.tool.enums.DbType;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GearFeatureClass
 *
 * @author GuoYingdong
 * @date 2022/03/03
 */
public class GearFeatureClass implements Iterable<GearFeature>{

    private final DataStore dataStore;
    @Getter
    private final SimpleFeatureCollection featureCollection;

    public GearFeatureClass(SimpleFeatureSource featureSource) throws IOException {
        this.featureCollection = featureSource.getFeatures();
        this.dataStore = (DataStore) featureSource.getDataStore();
    }

    public GearFeatureClass(SimpleFeatureCollection simpleFeatureCollection) {
        this.featureCollection = simpleFeatureCollection;
        this.dataStore = null;
    }

    public Integer getCount() {
        return this.featureCollection.size();
    }

    /**
     * 从 shpFile 读取要素类
     *
     * @param shpFile shp文件
     * @throws Exception 异常
     */
    public GearFeatureClass readShp(String shpFile) throws Exception {
        GearConnection gearConnection = new GearConnection(DbType.ShpFile, shpFile);
        GearDataStore gearDataStore = new GearDataStore(gearConnection);
        return gearDataStore.readFeatureClass();
    }

    public CoordinateReferenceSystem getCrs() {
        return this.featureCollection.getSchema().getCoordinateReferenceSystem();
    }

    public ReferencedEnvelope getBounds() {
        return this.featureCollection.getBounds();
    }

    /**
     * TODO
     *
     * @return {@link Stream}<{@link GearFeature}>
     */
    public Stream<GearFeature> getGearFeaturesStream() {
        throw new NotImplementedException();
    }

    public GearFeature getFeature(String fieldName, Object fieldValue) {
        for (GearFeature gearFea : this) {
            if (gearFea.getFieldValue(fieldName).equals(fieldValue)) {
                return gearFea;
            }
        }
        return null;
    }

    public GearFeature buildFeature(Geometry geometry, Map<String, Object> fields) {
        SimpleFeatureType schema = this.featureCollection.getSchema();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        return new GearFeature(feature);
    }

    public boolean addOneFeature(GearFeature feature) throws IOException {
        if (this.dataStore == null) {
            throw new RuntimeException("未定义数据源位置dataStore！");
        }
        CoordinateSystem coordinateSystem1 = feature.getCrs().getCoordinateSystem();
        CoordinateSystem coordinateSystem2 = this.featureCollection.getSchema().getCoordinateReferenceSystem().getCoordinateSystem();
        if (!coordinateSystem1.toWKT().equals(coordinateSystem2.toWKT())) {
            throw new RuntimeException("导入要素的坐标系与目标要素类不一致！");
        }
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend = dataStore.getFeatureWriterAppend(this.featureCollection.getSchema().getName().getLocalPart(), Transaction.AUTO_COMMIT)) {
            SimpleFeature targetFeature = featureWriterAppend.next();
            List<String> fieldNameList = this.getFieldNameList();
            for (String fieldName : fieldNameList) {
                targetFeature.setAttribute(fieldName, feature.getFieldValue(fieldName));
            }
            targetFeature.setDefaultGeometry(feature.getGeometry().copy());
            featureWriterAppend.write();
        }
        return true;
    }

    public boolean addFeatures(GearFeatureClass featureClass) throws IOException {
        this.addFeatures(featureClass, null);
        return true;
    }

    public boolean addFeatures(List<GearFeature> featureList) throws IOException {
        if (featureList.size() == 0) {
            return true;
        }
        CoordinateSystem coordinateSystem1 = featureList.get(0).getCrs().getCoordinateSystem();
        CoordinateSystem coordinateSystem2 = this.featureCollection.getSchema().getCoordinateReferenceSystem().getCoordinateSystem();
        if (!coordinateSystem1.toWKT().equals(coordinateSystem2.toWKT())) {
            throw new RuntimeException("导入要素的坐标系与目标要素类不一致！");
        }
        String schema = this.featureCollection.getSchema().getName().getLocalPart();
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend = dataStore.getFeatureWriterAppend(schema, Transaction.AUTO_COMMIT)) {
            for (GearFeature feature : featureList) {
                writeFeature(featureWriterAppend, feature, null);
            }
        }
        return true;
    }

    public boolean addFeatures(GearFeatureClass featureClass, Map<String, String> fieldNameMaps) throws IOException {
        CoordinateReferenceSystem crs1 = featureClass.getCrs();
        CoordinateSystem coordinateSystem1 = crs1 == null ? null : crs1.getCoordinateSystem();
        CoordinateReferenceSystem crs2 = this.featureCollection.getSchema().getCoordinateReferenceSystem();
        CoordinateSystem coordinateSystem2 = crs2 == null ? null : crs2.getCoordinateSystem();
        if (coordinateSystem1 != null
                && coordinateSystem2 != null
                && !coordinateSystem1.toWKT().equals(coordinateSystem2.toWKT())) {
            throw new RuntimeException("导入要素的坐标系与目标要素类不一致！");
        }
        String schema = this.featureCollection.getSchema().getName().getLocalPart();
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend = dataStore.getFeatureWriterAppend(schema, Transaction.AUTO_COMMIT)) {
            for (GearFeature feature : featureClass) {
                writeFeature(featureWriterAppend, feature, fieldNameMaps);
            }
        }
        return true;
    }

    private void writeFeature(FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend, GearFeature fea, Map<String, String> fieldNameMaps) {
        try {
            SimpleFeature targetFeature = featureWriterAppend.next();
            targetFeature.setDefaultGeometry(fea.getGeometry().clone());
            List<String> fieldNameList = this.getFieldNameList();
            for (String fieldName : fieldNameList) {
                String tempFieldName = fieldNameMaps != null ? fieldNameMaps.get(fieldName) : fieldName;
                Object fieldValue = fea.getFieldValue(tempFieldName);
                targetFeature.setAttribute(fieldName, fieldValue);
            }
            featureWriterAppend.write();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * TODO
     * 修复
     *
     * @return {@link Boolean}
     */
    public Boolean repair() {
        try {
            Geometry geometry = null;
            for (GearFeature fea : this) {
                geometry = fea.getGeometry();
                Geometry repair = GeometryUtils.repair(geometry);
                fea.setGeometry(repair);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> getFieldNameList() throws IOException {
        SimpleFeatureType featureType = this.featureCollection.getSchema();
        List<String> fieldNames = featureType.getAttributeDescriptors()
                .stream()
                .map(p -> p.getName().getLocalPart())
                .collect(Collectors.toList());
        return fieldNames;
    }

    public String toGeoJson() throws IOException {
        return GeoJSONWriter.toGeoJSON(this.featureCollection);
    }

    public GearFeatureClass intersect(GearFeatureClass featureClass) throws ExecutionException, InterruptedException, IOException {
        SimpleFeatureCollection intersect = FeatureClassUtil.intersect(this.featureCollection, featureClass.getFeatureCollection());
        // 保存到目标要素类
        SimpleFeatureType schema1 = this.featureCollection.getSchema();
        SimpleFeatureType schema2 = featureClass.getFeatureCollection().getSchema();
        List<Map<String, String>> nameMaps = FeatureClassUtil.getNameMaps(schema1, schema2);
        List<String> fieldNameList = nameMaps.stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
        GearFeatureClass memory = FeatureClassUtil.buildMemoryFeatureClass("memory",
                this.featureCollection.getSchema().getCoordinateReferenceSystem(),
                fieldNameList);
        // 划分区域
        List<GearFeatureClass> sourceFeatureClassList = new ArrayList<>();
        List<GearFeatureClass> targetFeatureClassList = new ArrayList<>();
        if (true) {
            // 未实现，没找到合适的实现方法
            throw new UnsupportedOperationException("未实现，没找到合适的实现方法");
        }
        // 划分 box
        ReferencedEnvelope bounds = this.featureCollection.getBounds();
        int size = this.featureCollection.size();
        double widthScale = Math.sqrt(size / 2000);
        double heightScale = widthScale * bounds.getHeight() / bounds.getWidth();
        double gridWidth = Math.ceil(bounds.getWidth() / widthScale);
        double gridHeight = Math.ceil(bounds.getHeight() / heightScale);
        for (int i = 0; i < widthScale; i++) {
            for (int j = 0; j < heightScale; j++) {
                double tempMinX = bounds.getMinX() + (i * gridWidth);
                double tempMinY = bounds.getMinY() + (j * gridHeight);
                double tempMaxX = bounds.getMinX() + ((i + 1) * gridWidth);
                double tempMaxY = bounds.getMinX() + ((i + 1) * gridHeight);
                BoundingBox box = new ReferencedEnvelope(tempMinX, tempMinY, tempMaxX, tempMaxY, bounds.getCoordinateReferenceSystem());

                FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
                // Filter filter = ff.bbox(ff.property("THE_GEOM"), ff.literal(box));
                Filter filter = ff.contains(ff.property("THE_GEOM"), ff.literal(box));
                SimpleFeatureCollection tempSource = this.featureCollection.subCollection(filter);
                SimpleFeatureCollection tempTarget = featureClass.getFeatureCollection().subCollection(filter);
                GearFeatureClass intersect1 = new GearFeatureClass(tempSource).intersect(new GearFeatureClass(tempTarget));
            }
        }

        // 分别获取 box 范围内数据
        // 针对落在 box 边界上的数据，再次进行 box*2.5 范围的分区，并获取各区域数据
        // 针对剩余数据单独作为一个区域集合
        // 将所有对应区域集合的数据，多线程进行相交处理

        // 各区块进行相交(此处可多线程处理)
        for (int i = 0; i < sourceFeatureClassList.size(); i++) {
            GearFeatureClass source = sourceFeatureClassList.get(i);
            GearFeatureClass target = targetFeatureClassList.get(i);
            if (source == null || target == null) {
                continue;
            }
            GearFeatureClass tempIntersect = source.intersect(target);
            memory.addFeatures(tempIntersect);
        }
        return memory;
    }

    public GearFeatureClass erase(GearFeatureClass featureClass) {
        SimpleFeatureCollection erase = FeatureClassUtil.erase(this.featureCollection, featureClass.getFeatureCollection());
        return new GearFeatureClass(erase);
    }

    public String exportShp(String shpFile) throws Exception {
        if (this.getCount() == 0) {
            return null;
        }
        shpFile = shpFile.endsWith(".shp") ? shpFile : shpFile + ".shp";
        File file = new File(shpFile);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        SimpleFeatureType ft = this.getFeatureCollection().getSchema();
        String fileName = ft.getTypeName();
        Map<String, Serializable> creationParams = new HashMap<>();
        creationParams.put("url", file.toURI().toURL());
        ShapefileDataStore dataStore = null;
        try (Transaction t = new DefaultTransaction()) {
            dataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(creationParams);
            dataStore.setCharset(Charset.defaultCharset());
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(this.getCrs());
            tb.setName("shapefile");
            tb.add("the_geom", MultiPolygon.class);
            dataStore.createSchema(tb.buildFeatureType());
            SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(ft.getTypeName());
            SimpleFeatureCollection collection = this.getFeatureCollection();
            featureStore.addFeatures(collection);
            t.commit(); // write it out
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataStore != null) {
                dataStore.dispose();
            }
        }
        return file.getAbsolutePath();
    }

    @Override
    @SneakyThrows
    public Iterator<GearFeature> iterator() {
        SimpleFeatureIterator simpleFeatureIterator = featureCollection.features();
        return new Iterator<GearFeature>() {
            @SneakyThrows
            @Override
            public boolean hasNext() {
                boolean hasNext = simpleFeatureIterator.hasNext();
                if (!hasNext) {
                    simpleFeatureIterator.close();
                }
                return hasNext;
            }

            @SneakyThrows
            @Override
            public GearFeature next() {
                SimpleFeature next = simpleFeatureIterator.next();
                return new GearFeature(next);
            }

            @Override
            protected void finalize() throws Throwable {
                simpleFeatureIterator.close();
                super.finalize();
            }
        };

    }
}
