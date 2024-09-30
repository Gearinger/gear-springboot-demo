package com.gear.sqlite.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private String address;

    private String avatar;

    private String role;

    private String enable;

}
