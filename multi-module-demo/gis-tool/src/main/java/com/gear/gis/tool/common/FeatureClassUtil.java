package com.gear.gis.tool.common;

import com.gear.gis.tool.interfaces.IGearFeatureClass;
import com.gear.gis.tool.interfaces.impl.GearFeatureClass;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FeatureClassUtil {
    public static IGearFeatureClass buildMemoryFeatureClass(String name, CoordinateReferenceSystem crs, List<String> fieldNameList) throws IOException {
        SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setCRS(crs);
        featureTypeBuilder.setName(name);
        List<AttributeDescriptor> attributeDescriptorList = fieldNameList.stream()
                .map(p -> new AttributeTypeImpl(new NameImpl(p), String.class, false, false, null, null, null))
                .map(p -> (AttributeDescriptor) new AttributeDescriptorImpl(p, p.getName(), 0, 0, true, null))
                .collect(Collectors.toList());
        featureTypeBuilder.setAttributes(attributeDescriptorList);
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();

        // Create target schema
        SimpleFeatureTypeBuilder buildType = new SimpleFeatureTypeBuilder();
        buildType.init(featureType);
        buildType.setName(name);
        buildType.setCRS(featureType.getCoordinateReferenceSystem());

        final SimpleFeatureType schema = buildType.buildFeatureType();
        // Configure memory datastore
        final MemoryDataStore memoryDataStore = new MemoryDataStore();
        memoryDataStore.createSchema(schema);
        ContentFeatureSource featureSource = memoryDataStore.getFeatureSource(name);
        return new GearFeatureClass(featureSource);
    }

    /**
     * 相交(针对小范围的数据)
     *
     * @param featureCollection1 特色文物
     * @param featureCollection2 功能collection2
     * @return {@link SimpleFeatureCollection}
     * @throws ExecutionException   执行异常
     * @throws InterruptedException 中断异常
     */
    public static SimpleFeatureCollection intersect(SimpleFeatureCollection featureCollection1, SimpleFeatureCollection featureCollection2) throws ExecutionException, InterruptedException {
        if (featureCollection1 == null || featureCollection2 == null) {
            return null;
        }
        SimpleFeatureType schema1 = featureCollection1.getSchema();
        SimpleFeatureType schema2 = featureCollection2.getSchema();
        List<Map<String, String>> nameMaps = getNameMaps(schema1, schema2);
        SimpleFeatureTypeBuilder featureTypeBuilder = getFeatureTypeBuilder(schema1, schema2, nameMaps);
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        MemoryFeatureCollection featureCollectionResult = new MemoryFeatureCollection(featureType);
        SimpleFeatureIterator featureIterator1 = null;
        SimpleFeatureIterator featureIterator2 = null;
        try {
            featureIterator1 = featureCollection1.features();
            if (featureCollection2.getSchema().getGeometryDescriptor() == null) {
                return featureCollectionResult;
            }
            String localName = featureCollection2.getSchema().getGeometryDescriptor().getLocalName();
            SimpleFeature next1;
            Geometry geometry;
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
            Filter filter;
            SimpleFeatureCollection featureCollection;
            SimpleFeature next2;
            Geometry geometry1;
            Geometry intersection;
            SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(featureType);
            SimpleFeature feature;
            Integer num =0;
            while (featureIterator1.hasNext()) {
                next1 = featureIterator1.next();
                geometry = (Geometry) next1.getDefaultGeometry();
                filter = ff.intersects(ff.property(localName), ff.literal(geometry));
                featureCollection = featureCollection2.subCollection(filter);

                featureIterator2 = featureCollection.features();
                while (featureIterator2.hasNext()) {
                    num++;
                    System.out.println(num);
                    next2 = featureIterator2.next();
                    geometry1 = (Geometry) next2.getDefaultGeometry();
                    try {
                        intersection = geometry1.intersection(geometry);
                    } catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }
                    if (intersection.isEmpty()) {
                        continue;
                    }
                    feature = simpleFeatureBuilder.buildFeature(null);
                    for (String key : nameMaps.get(0).keySet()) {
                        feature.setAttribute(nameMaps.get(0).get(key), next1.getAttribute(key));
                    }
                    for (String key : nameMaps.get(1).keySet()) {
                        feature.setAttribute(nameMaps.get(1).get(key), next2.getAttribute(key));
                    }
                    feature.setDefaultGeometry(intersection);
                    featureCollectionResult.add(feature);
                }
                featureIterator2.close();
            }
        } finally {
            if (featureIterator1 != null) {
                featureIterator1.close();
            }
            if (featureIterator2 != null) {
                featureIterator2.close();
            }
        }

        return featureCollectionResult;
    }


    public static SimpleFeatureCollection erase(SimpleFeatureCollection featureCollection1, SimpleFeatureCollection featureCollection2) {
        SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        SimpleFeatureType schema1 = featureCollection1.getSchema();
        featureTypeBuilder.setCRS(schema1.getCoordinateReferenceSystem());
        List<AttributeDescriptor> attributeDescriptors1 = schema1.getAttributeDescriptors();
        Map<String, String> a1Map = attributeDescriptors1
                .stream()
                .filter(p -> !p.getLocalName().equals(schema1.getGeometryDescriptor().getLocalName()))
                .collect(Collectors.toMap(AttributeDescriptor::getLocalName, AttributeDescriptor::getLocalName));
        featureTypeBuilder.addAll(attributeDescriptors1);
        featureTypeBuilder.setName(schema1.getName() + "_erase");
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        MemoryFeatureCollection featureCollectionResult = new MemoryFeatureCollection(featureType);
        SimpleFeatureIterator featureIterator1 = null;
        SimpleFeatureIterator featureIterator2 = null;
        try {
            featureIterator1 = featureCollection1.features();
            String localName = featureCollection2.getSchema().getGeometryDescriptor().getLocalName();
            while (featureIterator1.hasNext()) {
                SimpleFeature next1 = featureIterator1.next();
                Geometry geometry = (Geometry) next1.getDefaultGeometry();
                FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
                Filter filter = ff.intersects(ff.property(localName), ff.literal(geometry));
                SimpleFeatureCollection featureCollection = featureCollection2.subCollection(filter);

                featureIterator2 = featureCollection.features();
                while (featureIterator2.hasNext()) {
                    SimpleFeature next2 = featureIterator2.next();
                    Geometry geometry1 = (Geometry) next2.getDefaultGeometry();
                    Geometry difference = geometry1.difference(geometry);
                    SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(featureType);
                    SimpleFeature feature = simpleFeatureBuilder.buildFeature(null);
                    for (String key : a1Map.keySet()) {
                        feature.setAttribute(a1Map.get(key), next1.getAttribute(key));
                    }
                    feature.setDefaultGeometry(difference);
                    featureCollectionResult.add(feature);
                }
                featureIterator2.close();
            }
        } finally {
            if (featureIterator1 != null) {
                featureIterator1.close();
            }
            if (featureIterator2 != null) {
                featureIterator2.close();
            }
        }
        return featureCollectionResult;
    }

    private static SimpleFeatureTypeBuilder getFeatureTypeBuilder(SimpleFeatureType schema1, SimpleFeatureType schema2, List<Map<String, String>> nameMaps){
        SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setCRS(schema1.getCoordinateReferenceSystem());
        List<AttributeDescriptor> attributeDescriptors1 = schema1.getAttributeDescriptors();
        List<AttributeDescriptor> attributeDescriptors2 = schema2.getAttributeDescriptors();
        List<AttributeDescriptor> attributeDescriptors2Res = new ArrayList<>();
        for (AttributeDescriptor a2 : attributeDescriptors2) {
            String localName2 = a2.getLocalName();
            if (schema2.getGeometryDescriptor() != null && localName2.equals(schema2.getGeometryDescriptor().getLocalName())) {
                continue;
            }
            Optional<AttributeDescriptor> any = attributeDescriptors1.stream().filter(p -> p.getLocalName().equals(localName2)).findAny();
            if (any.isPresent()) {
                String newName = nameMaps.get(1).get(localName2);
                AttributeDescriptor attributeDescriptor = new AttributeDescriptorImpl(a2.getType(), new NameImpl(newName), a2.getMinOccurs(), a2.getMaxOccurs(), a2.isNillable(), a2.getDefaultValue());
                attributeDescriptors2Res.add(attributeDescriptor);
            }
        }
        featureTypeBuilder.addAll(attributeDescriptors1);
        featureTypeBuilder.addAll(attributeDescriptors2Res);
        featureTypeBuilder.setName(schema1.getName() + "_intersect");
        return featureTypeBuilder;
    }

    public static List<Map<String, String>> getNameMaps(SimpleFeatureType schema1, SimpleFeatureType schema2){
        List<AttributeDescriptor> attributeDescriptors1 = schema1.getAttributeDescriptors();
        Map<String, String> a1Map = attributeDescriptors1
                .stream()
                .filter(p -> schema1.getGeometryDescriptor() != null && !p.getLocalName().equals(schema1.getGeometryDescriptor().getLocalName()))
                .collect(Collectors.toMap(AttributeDescriptor::getLocalName, AttributeDescriptor::getLocalName));
        List<AttributeDescriptor> attributeDescriptors2 = schema2.getAttributeDescriptors();
        Map<String, String> a2Map = new HashMap<>();
        List<AttributeDescriptor> attributeDescriptors2Res = new ArrayList<>();
        for (AttributeDescriptor a2 : attributeDescriptors2) {
            String localName2 = a2.getLocalName();
            if (schema2.getGeometryDescriptor() != null && localName2.equals(schema2.getGeometryDescriptor().getLocalName())) {
                continue;
            }
            Optional<AttributeDescriptor> any = attributeDescriptors1.stream().filter(p -> p.getLocalName().equals(localName2)).findAny();
            if (any.isPresent()) {
                String newName = localName2 + "_t";
                a2Map.put(localName2, newName);
                AttributeDescriptor attributeDescriptor = new AttributeDescriptorImpl(a2.getType(), new NameImpl(newName), a2.getMinOccurs(), a2.getMaxOccurs(), a2.isNillable(), a2.getDefaultValue());
                attributeDescriptors2Res.add(attributeDescriptor);
            }
        }
        List<Map<String, String>> nameMaps = new ArrayList<>();
        nameMaps.add(a1Map);
        nameMaps.add(a2Map);
        return nameMaps;
    }

}


