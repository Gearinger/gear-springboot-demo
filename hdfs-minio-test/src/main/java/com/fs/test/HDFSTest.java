package com.fs.test;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class HDFSTest {
    final static String hdfsUrl = "hdfs://localhost:8020";
    private static FileSystem fs = null;
    private String bucketName;

    public HDFSTest(String bucketName) throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsUrl);
        fs = FileSystem.get(new URI(hdfsUrl), new Configuration(), bucketName);

        this.bucketName = bucketName;
    }

    public void uploadLargeNumberFile(String rootPath) throws URISyntaxException, IOException, InterruptedException {

        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                String filePath = file.getAbsolutePath();
                String targetPath = filePath.replace(rootPath, "");
                try {
                    fs.copyFromLocalFile(false, new Path(filePath), new Path(targetPath));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void uploadLargeNumberFileByPool(String rootPath, ThreadPoolExecutor poolExecutor) throws URISyntaxException, IOException, InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        FileUtil.walkFiles(new File(rootPath), file -> {
            if (file.isFile()) {
                atomicInteger.incrementAndGet();

                String filePath = file.getAbsolutePath();
                String targetPath = filePath.replace(rootPath, "");

                try {
                    poolExecutor.submit(() -> {
                        try {
                            try {
                                fs.copyFromLocalFile(false, new Path(filePath), new Path(targetPath));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
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

    public long readFileTest() throws IOException {
        List<Path> pathList = new ArrayList<>();
        int index = 0;
        int turnCount = 100;

        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/"), true);
        while (iterator.hasNext() && index < turnCount) {
            LocatedFileStatus next = iterator.next();
            if (next.isFile()) {
                pathList.add(next.getPath());
                index++;
            }
        }
        // 测试访问时间
        long totalTime = 0, start;
        for (Path path : pathList) {
            start = System.currentTimeMillis();
            FSDataInputStream inputStream = fs.open(path);
            int read = inputStream.read();
            totalTime += (System.currentTimeMillis() - start);
        }
        return totalTime / turnCount;
    }
}
