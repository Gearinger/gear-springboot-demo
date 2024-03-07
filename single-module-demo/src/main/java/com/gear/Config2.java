package com.gear;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "config2")
public class Config2 {
    public Config2() {
        this.temp = new Temp();
    }

    private String name;

    private final Temp temp;

    @Data
    @ToString
    @Configuration
    @ConfigurationProperties(prefix = "config2.temp")
    public static class Temp {
        private String childName = "……";
    }
}
