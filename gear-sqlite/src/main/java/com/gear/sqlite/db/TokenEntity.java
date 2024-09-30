package com.gear.sqlite.db;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "token")
public class TokenEntity {

    @Id
    private String token;

    private Integer userId;

    private String refreshToken;

    private String loginIp;

    private String loginTime;

    private String loginLocation;

    private String browser;

    private LocalDateTime expireTime;

    private LocalDateTime refreshExpireTime;

}
