import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.tomcat.util.bcel.Const;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HdfsTest {

    final static String hdfsUrl = "hdfs://localhost:8020";


    @Test
    public void testLargeNumberFileUpload() throws URISyntaxException, IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));
        var rootPath = "E:/Data/wuhan-polygon-tile";
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        FileSystem fs = FileSystem.get(new URI(hdfsUrl), new Configuration(), "test");
        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                String fileName = file.getName();
                String filePath = file.getAbsolutePath();
                String targetPath = file.getAbsolutePath().replace("E:\\", "").replace("\\", "/");
                try {
                    fs.copyFromLocalFile(false, new Path(filePath), new Path(targetPath));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        long allTime = System.currentTimeMillis() - startTime;
        System.out.println(new DateTime(allTime));
    }

    @Test
    public void testLargeNumberFileUploadByPool() throws URISyntaxException, IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));
        var rootPath = "E:/Data/wuhan-polygon-tile";
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        FileSystem fs = FileSystem.get(new URI(hdfsUrl), new Configuration(), "test");
        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                String fileName = file.getName();
                String filePath = file.getAbsolutePath();
                String targetPath = file.getAbsolutePath().replace("E:\\", "").replace("\\", "/");
                try {
                    fs.copyFromLocalFile(false, new Path(filePath), new Path(targetPath));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        long allTime = System.currentTimeMillis() - startTime;
        System.out.println(new DateTime(allTime));
    }

}
