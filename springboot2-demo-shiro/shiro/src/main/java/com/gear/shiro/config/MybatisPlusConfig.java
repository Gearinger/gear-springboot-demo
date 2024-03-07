package com.gear.shiro.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * MybatisPlus配置
 *
 * @author xiongneng
 * @since 2017/5/20 21:58
 */
@Configuration
@EnableTransactionManagement(order = 2)
@MapperScan(basePackages = {"com.gear.shiro.dao"})
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusInterceptor();
    }
}
