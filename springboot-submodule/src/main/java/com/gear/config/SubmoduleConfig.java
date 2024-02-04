package com.gear.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * 子模块配置
 *
 * @author guoyingdong
 * @date 2024/02/04
 */
@Configuration
@ConfigurationProperties(prefix = "submodule")
@PropertySource(value = "classpath:submodule-application.yml", factory = MinePropertySourceFactory.class)
public class SubmoduleConfig {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
