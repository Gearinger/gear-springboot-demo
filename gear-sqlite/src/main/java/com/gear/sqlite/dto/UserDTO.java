package com.gear.sqlite.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Integer userId;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private String address;

    private String avatar;

    private String role;

    private Boolean enable;

    private String token;

    private String refreshToken;

    private String loginIp;

    private String loginTime;

    private String loginLocation;

    private String browser;

}
