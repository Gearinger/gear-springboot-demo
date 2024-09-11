package com.gear.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author baofuen
 * @version v1.0
 * @create 2024-07-15 13:52
 * @project gis-basis
 * @description LicenseConfiguration-证书认证
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "sys.license.server")
public class LicenseServerConfig {

    private String licensePrivateKey;

}
