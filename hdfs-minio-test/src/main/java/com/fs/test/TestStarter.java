package com.fs.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import io.minio.errors.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestStarter {

    private static final String TILES_PATH = "D:\\Test\\FileSystemTest\\16MB10123";

    private static final String BUCKET_NAME = FileUtil.getName(TILES_PATH).toLowerCase();

    public static void main(String[] args) throws IOException, InterruptedException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, URISyntaxException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 20, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        long startTime, allTime;

        // 测试 HDFS
        System.out.println("--------------测试 HDFS----------------");
        startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));

        new HDFSTest(BUCKET_NAME).uploadLargeNumberFile(TILES_PATH);

        allTime = System.currentTimeMillis() - startTime;
        System.out.println(allTime / 1000.0);

        // 测试 HDFS 多线程
        System.out.println("--------------测试 HDFS 多线程----------------");
        startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));

        new HDFSTest(BUCKET_NAME).uploadLargeNumberFileByPool(TILES_PATH, poolExecutor);

        allTime = System.currentTimeMillis() - startTime;
        System.out.println(allTime / 1000.0);


        // 测试 MinIO
        System.out.println("--------------测试 MinIO----------------");
        startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));

        new MinioTest(BUCKET_NAME).uploadLargeNumberFile(TILES_PATH);

        allTime = System.currentTimeMillis() - startTime;
        System.out.println(allTime/1000.0);

        // 测试 MinIO 多线程
        System.out.println("--------------测试 MinIO 多线程----------------");
        System.out.println("线程数：" + poolExecutor.getCorePoolSize());
        startTime = System.currentTimeMillis();
        System.out.println(new DateTime(startTime));

        new MinioTest(BUCKET_NAME + "pool").uploadLargeNumberFileByPool(TILES_PATH, poolExecutor);

        allTime = System.currentTimeMillis() - startTime;
        System.out.println(allTime / 1000.0);
        
        
        // 访问测试
        long hdfsReadTime = new HDFSTest(BUCKET_NAME).readFileTest();
        System.out.println("HDFS 访问耗时：" + hdfsReadTime);
        long minioReadTime = new MinioTest(BUCKET_NAME).readFileTest();
        System.out.println("MinIO访问耗时：" + hdfsReadTime);

    }
}
