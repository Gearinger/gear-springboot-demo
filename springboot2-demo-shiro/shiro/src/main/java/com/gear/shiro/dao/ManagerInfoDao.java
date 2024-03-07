package com.gear.shiro.dao;


import com.gear.shiro.entity.ManagerInfo;
import com.gear.shiro.repository.ManagerMapper;
import org.springframework.stereotype.Component;

/**
 * Description  :
 */
@Component
public interface ManagerInfoDao extends ManagerMapper {
    ManagerInfo findByUsername(String username);
}
