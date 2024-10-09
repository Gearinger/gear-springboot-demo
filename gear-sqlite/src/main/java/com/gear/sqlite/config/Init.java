package com.gear.sqlite.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class Init {

    private final SystemConfig systemConfig;

    @PostConstruct
    public void init() {
        log.info("系统初始化开始");

        initStoragePath();

        log.info("系统初始化完成");
    }

    private void initStoragePath() {
        log.info("初始化存储路径:{}", systemConfig.getStoragePath());
        String storagePath = systemConfig.getStoragePath();
        File file = new File(storagePath);
        if (!file.exists()) {
            log.info("创建文件目录:{}", storagePath);
            file.mkdirs();
        }
    }

}
