package com.gear.poi.search.pgvector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@SpringBootApplication
public class PgVectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgVectorApplication.class, args);
    }

}