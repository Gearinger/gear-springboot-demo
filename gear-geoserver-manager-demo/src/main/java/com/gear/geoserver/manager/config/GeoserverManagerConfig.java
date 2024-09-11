package com.gear.geoserver.manager.config;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.http.GeoServerRestAuthenticator;
import it.geosolutions.geoserver.rest.http.UsernamePasswordAuthenticator;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStyleManager;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 地理服务器管理器配置
 *
 * @author guoyingdong
 * @date 2024/07/03
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "geoserver.manager")
public class GeoserverManagerConfig {

    private String url = "http://192.168.100.123:20191/geoserver/";

    private String username = "admin";

    private String password = "geoserver";

    @Bean
    public GeoServerRestAuthenticator geoServerRestAuthenticator() {
        return new UsernamePasswordAuthenticator(username, password);
    }

    @Bean
    public GeoServerRESTReader geoServerRestReader() throws MalformedURLException {
        GeoServerRestAuthenticator authenticator =  geoServerRestAuthenticator();
        return new GeoServerRESTReader(url, authenticator);
    }

    @Bean
    public GeoServerRESTPublisher geoServerRestPublisher() {
        GeoServerRestAuthenticator authenticator =  geoServerRestAuthenticator();
        return new GeoServerRESTPublisher(url, authenticator);
    }

    @Bean
    public GeoServerRESTStoreManager geoServerRestStoreManager() throws MalformedURLException {
        GeoServerRestAuthenticator authenticator =  geoServerRestAuthenticator();
        return new GeoServerRESTStoreManager(new URL(url), authenticator);
    }

    @Bean
    public GeoServerRESTStyleManager geoServerRestStyleManager() throws MalformedURLException {
        GeoServerRestAuthenticator authenticator =  geoServerRestAuthenticator();
        return new GeoServerRESTStyleManager(new URL(url), authenticator);
    }

}
