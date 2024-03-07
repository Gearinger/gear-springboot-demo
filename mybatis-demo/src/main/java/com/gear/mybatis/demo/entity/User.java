package com.gear.mybatis.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public User(String name){
        this.name = name;
    }

    private  int id;
    private String name;

    }
