package com.gear;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "file")
public class Config {

    public Config() {
        temp = new Temp();
    }

    private String path;

    private final Temp temp;

    @Data
    @ToString
    public static class Temp {
        private String linuxPath;
        private String windowsPath;
    }
}
