package com.gear.sqlite.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 系统配置
 *
 * @author guoyingdong
 * @date 2024/09/30
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    @PostConstruct
    public void init() {
        File file = new File(storagePath);
        if (!file.exists()) {
            log.info("创建文件目录:{}", storagePath);
            file.mkdirs();
        }
    }

    private String storagePath = "./data/storage";

    /**
     * 过期时间 小时
     */
    private Integer expireTime = 24;

    /**
     * 刷新过期时间 小时
     */
    private Integer refreshExpireTime = 25;

    private Boolean enableAuth = true;

}
