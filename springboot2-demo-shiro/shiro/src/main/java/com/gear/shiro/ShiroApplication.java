package com.gear.shiro;

import com.gear.shiro.dao.ManagerInfoDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gear*"})
@MapperScan("com.gear.shiro.dao.mapping")
public class ShiroApplication {

    @Autowired
    ManagerInfoDao managerInfoDao;

    public static void main(String[] args) {
        SpringApplication.run(ShiroApplication.class, args);
    }

}
