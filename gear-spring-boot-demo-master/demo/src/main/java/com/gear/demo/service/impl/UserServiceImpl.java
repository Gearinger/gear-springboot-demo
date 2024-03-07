package com.gear.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gear.demo.entity.User;
import com.gear.demo.mapper.UserMapper;
import com.gear.demo.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GuoYingdong
 * @since 2022-02-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
