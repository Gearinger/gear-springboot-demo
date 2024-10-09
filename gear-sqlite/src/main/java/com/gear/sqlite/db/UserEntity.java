package com.gear.sqlite.db;

import com.gear.sqlite.common.RoleEnumConverter;
import com.gear.sqlite.enums.RoleEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名格式不正确")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[^\\s]{6,16}$", message = "密码格式不正确")
    private String password;

    private String nickname;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String address;

    private String avatar;

    @Convert(converter = RoleEnumConverter.class)
    private RoleEnum role;

    private String enable;

}
