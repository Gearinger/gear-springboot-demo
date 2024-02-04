package com.fs.test;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


public class MinioTest {

    private MinioClient minioClient = null;

    public static String minioUrl = "http://127.0.0.1:9000";
    public static String accessKey = "minioadmin";
    public static String secretKey = "minioadmin";

    private String bucketName = "test";

    public MinioTest(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        this.bucketName = bucketName;
        if (minioClient == null) {
            minioClient = MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(accessKey, secretKey)
                    .build();
        }
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    public void uploadLargeNumberFile(String rootPath) {
        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                String fileName = file.getName().replace(".", "");
                String filePath = file.getAbsolutePath();
                String targetPath = filePath.replace(rootPath, "");
                try {
                    minioClient.uploadObject(UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(targetPath)
                            .filename(filePath)
                            .build());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void uploadLargeNumberFileByPool(String rootPath, ThreadPoolExecutor poolExecutor) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                atomicInteger.incrementAndGet();

                String filePath = file.getAbsolutePath();
                String targetPath = filePath.replace(rootPath, "");

                try {
                    poolExecutor.submit(() -> {
                        try {
                            minioClient.uploadObject(UploadObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(targetPath)
                                    .filename(filePath)
                                    .build());
                        } catch (Exception e) {
                            System.out.println(ExceptionUtil.stacktraceToString(e));
                            throw new RuntimeException(e);
                        } finally {
                            atomicInteger.decrementAndGet();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(ExceptionUtil.stacktraceToString(e));
                    throw new RuntimeException(e);
                }
            }
        });
        while (atomicInteger.get() > 0) {
//            System.out.println("wait..." + atomicInteger.get());
        }
    }

    public long readFileTest() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<String> pathList = new ArrayList<>();
        int index = 0;
        int turnCount = 100;

        Iterator<Result<Item>> iterator = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .build()).iterator();
        while (iterator.hasNext() && index < turnCount) {
            Result<Item> next = iterator.next();
            if (!next.get().isDir()) {
                pathList.add(next.get().objectName());
                index++;
            }
        }
        // 测试访问时间
        long totalTime = 0, start;
        for (String path : pathList) {
            start = System.currentTimeMillis();
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build());
            int read = object.read();
            totalTime += (System.currentTimeMillis() - start);
        }
        return totalTime / turnCount;
    }
}
