package com.gear.gis.tool.interfaces;

import org.geotools.data.simple.SimpleFeatureIterator;

import java.io.IOException;
import java.util.Map;

/**
 * 要素集
 *
 * @author GuoYingdong
 * @date 2022/03/03
 */
public interface IGearDataStore extends AutoCloseable {

    /**
     * 读取要素类
     *
     * @param featureClassName 特性类名
     * @return {@link SimpleFeatureIterator}
     * @throws IOException ioexception
     */
    IGearFeatureClass readFeatureClass(String featureClassName) throws IOException;

    /**
     * 读取默认的第一个要素类
     *
     * @return {@link SimpleFeatureIterator}
     * @throws IOException ioexception
     */
    IGearFeatureClass readFeatureClass() throws IOException;


    /**
     * 列出所有要素类名称
     *
     * @return {@link String[]}
     * @throws IOException ioexception
     */
    String[] listFeatureClassName() throws IOException;

    /**
     * 导入要素类
     *
     * @param sourceFeatureIterator  源要素类
     * @param targetFeatureClassName 目标要素类名称
     * @param fieldNameMaps          字段映射关系
     * @return {@link Boolean}
     */
    Boolean importFeatureClass(SimpleFeatureIterator sourceFeatureIterator, String targetFeatureClassName, Map<String, String> fieldNameMaps);

    /**
     * 导入要素类
     *
     * @param sourceFeatureIterator  源要素类
     * @param targetFeatureClassName 目标要素类名称
     * @return {@link Boolean}
     */
    Boolean importFeatureClass(SimpleFeatureIterator sourceFeatureIterator, String targetFeatureClassName);
}
