package com.gear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfiguration {

    /**
     * <p>Description：跨域过滤器</p>
     */
    @Bean
    public CorsFilter corsFilter() {
        //当前跨越请求最大有效时长，这里默认1小时
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
        corsConfiguration.setAllowCredentials(false);
        //1.设置访问源地址，*表示所有IP
        corsConfiguration.addAllowedOriginPattern("*/");
        corsConfiguration.setAllowCredentials(true);
        //2.设置访问源请求头，*表示所有IP
        corsConfiguration.addAllowedHeader("*");
        //3.设置访问源请求方法，*表示所有IP
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("*");
        //4.对接口配置跨域设置
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
