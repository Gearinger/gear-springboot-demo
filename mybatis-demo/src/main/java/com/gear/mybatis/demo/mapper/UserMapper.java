package com.gear.mybatis.demo.mapper;


import com.gear.mybatis.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户映射器
 *
 * @author guoyd
 * @version 1.0.0
 * @date 2021/02/26
 */
@Mapper
@Repository
public interface UserMapper {
    List<User> queryUserList();

    User queryUserById(int id);

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(int id);
}
