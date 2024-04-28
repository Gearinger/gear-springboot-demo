package com.gear.poi.search.pgvector.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Component
@AutoConfigureAfter(DataSource.class)
public class InitTable {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        log.info("--------------------初始化POI数据表--------------------");
        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS vector ;");
        jdbcTemplate.execute("ALTER TABLE poi ADD COLUMN IF NOT EXISTS words_vec vector(300); ");
        log.info("--------------------初始化POI数据表完成--------------------");
    }

}
