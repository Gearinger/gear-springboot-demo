import com.gear.gis.tool.enums.DbType;
import com.gear.gis.tool.interfaces.IGearConnection;
import com.gear.gis.tool.interfaces.IGearDataStore;
import com.gear.gis.tool.interfaces.IGearFeature;
import com.gear.gis.tool.interfaces.IGearFeatureClass;
import com.gear.gis.tool.interfaces.impl.GearConnection;
import com.gear.gis.tool.interfaces.impl.GearDataStore;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    @org.junit.jupiter.api.Test
    public void test() {
        // 连接信息
        GearConnection conn = new GearConnection(DbType.ShpFile,
                "D:\\MyWork\\Project\\FieldManageSystem\\Data\\20220302\\RE2022020102 (4)\\2022-03-01-15-50-15.shp");
        // 数据库
        try (GearDataStore dataStore = new GearDataStore(conn)) {
            // 要素集
            IGearFeatureClass featureClass = dataStore.readFeatureClass();
//            System.out.println(featureClass.getFieldNameList());

            // 连接信息
            GearConnection conn2 = new GearConnection(DbType.ShpFile,
                    "D:\\MyWork\\Project\\FieldManageSystem\\Data\\20220302\\RE2022020102 (1)\\2022-03-01-15-50-15.shp");
            // 数据库
            try (GearDataStore dataStore2 = new GearDataStore(conn2)) {
                // 要素集
                IGearFeatureClass featureClass2 = dataStore2.readFeatureClass();
//                Map<String, String> objectObjectHashMap = new HashMap<>();
//                objectObjectHashMap.put("ID", "ID");
//                featureClass2.addFeatures(featureClass, objectObjectHashMap);
                IGearFeatureClass erase = featureClass2.erase(featureClass);
                List<String> fieldNameList = erase.getFieldNameList();
                for (IGearFeature fea : erase) {
                    System.out.println(fea.getGeometry().getArea());
                }
                System.out.println(fieldNameList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    public void testPostGIS() throws Exception {
        IGearConnection connection = new GearConnection(DbType.PostGIS, "127.0.0.1", "5432", "test", "postgres", "postgres");
        IGearDataStore dataStore = new GearDataStore(connection);
        String[] strings = dataStore.listFeatureClassName();
        System.out.println(Arrays.stream(strings).collect(Collectors.toList()));

        IGearFeatureClass test = dataStore.readFeatureClass("test_field");
        IGearFeatureClass intersect = test.intersect(test);
        for (IGearFeature fea : intersect) {
            fea.getFieldValue("ntlx");
        }
        intersect.exportShp("./temp/test/");
    }


    @org.junit.jupiter.api.Test
    public void test2(){
        Point a  = new Point(0,0);
        Point b = a;
        Point c = b;
        a = new Point(1,2);
        System.out.println("a" + a.toString());
        System.out.println("b" + b.toString());
        System.out.println("c" + c.toString());
    }
}
