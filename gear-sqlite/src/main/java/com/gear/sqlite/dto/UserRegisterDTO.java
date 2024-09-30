package com.gear.sqlite.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private String address;
}
