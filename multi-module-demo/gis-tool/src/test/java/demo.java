import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class demo {

    /**
     * 读取 shp 文件
     *
     * @param shpFile shp文件
     * @return {@link SimpleFeatureIterator}
     * @throws IOException ioexception
     */
    public static SimpleFeatureIterator readShapefile(String shpFile) throws IOException {
        File file = new File(shpFile);
        ShapefileDataStore shpDataStore = null;
        shpDataStore = new ShapefileDataStore(file.toURI().toURL());
        //设置编码
        Charset charset = Charset.forName("GBK");
        shpDataStore.setCharset(charset);
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = null;
        featureSource = shpDataStore.getFeatureSource(typeName);
        SimpleFeatureCollection result = featureSource.getFeatures();
        return result.features();
    }

    /**
     * 根据字段映射关系转换要素
     *
     * @param fieldNameMaps 映射关系
     * @param sourceFeature 源要素
     * @param targetFeature 目标要素
     * @return {@link SimpleFeature}
     * @throws Exception 异常
     */
    public static SimpleFeature transformFeature(SimpleFeature sourceFeature, SimpleFeature targetFeature, Map<String, String> fieldNameMaps) throws Exception {
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

    /**
     * 根据字段映射关系转换要素
     *
     * @param sourceFeature 源要素
     * @param targetFeature 目标要素
     * @return {@link SimpleFeature}
     * @throws Exception 异常
     */
    public static SimpleFeature transformFeature(SimpleFeature sourceFeature, SimpleFeature targetFeature) throws Exception {
        return transformFeature(sourceFeature, targetFeature, null);
    }

    /**
     * 将要素集导入数据源
     *
     * @param sourceFeatureIterator 要素集
     * @param dataStore             数据集
     * @param targetName            目标表名
     * @param fieldNameMaps         字段映射关系
     * @return {@link Boolean}
     * @throws Exception 异常
     */
    public static Boolean writeToDatasource(SimpleFeatureIterator sourceFeatureIterator, DataStore dataStore, String targetName, Map<String, String> fieldNameMaps) throws Exception {
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriterAppend = dataStore.getFeatureWriterAppend(targetName, Transaction.AUTO_COMMIT)) {
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

    /**
     * 连接数据存储
     *
     * @param dbType   postgis
     * @param host     localhost
     * @param port     5432
     * @param dataBase 数据库名
     * @param username 用户名
     * @param passwd   密码
     * @return {@link DataStore}
     */
    public static DataStore connectDataStore(String dbType, String host, String port, String dataBase, String username, String passwd) throws IOException {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(PostgisNGDataStoreFactory.DBTYPE.key, dbType);
            params.put(PostgisNGDataStoreFactory.HOST.key, host);
            params.put(PostgisNGDataStoreFactory.PORT.key, port);
            //数据库名
            params.put(PostgisNGDataStoreFactory.DATABASE.key, dataBase);
            //用户名和密码
            params.put(PostgisNGDataStoreFactory.USER.key, username);
            params.put(PostgisNGDataStoreFactory.PASSWD.key, passwd);
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            return dataStore;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("连接数据库失败！请检查连接信息！");
        }
    }

    public static SimpleFeatureIterator readFeatureClass(DataStore dataStore, String featureClassName) throws IOException {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(featureClassName);
        SimpleFeatureCollection result = featureSource.getFeatures();
        return result.features();
    }
}
