package com.gear.gateway.config;

import com.gear.gateway.component.RestAuthenticationEntryPoint;
import com.gear.gateway.component.RestfulAccessDeniedHandler;
import com.gear.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;

/**
 * 资源服务配置
 * @author hefan
 * @date 2020/8/18 10:25
 */
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class ResourceServerConfigurer {

    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // 自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
        // 对白名单路径，直接移除JWT请求头
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter,
                SecurityWebFiltersOrder.AUTHENTICATION);

        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(),String.class)).permitAll()//白名单配置
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and().exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler) //处理未授权
                .authenticationEntryPoint(restAuthenticationEntryPoint) //处理未认证
                .and().csrf().disable();

        http.oauth2ResourceServer().jwt();

        return http.build();
    }
}
