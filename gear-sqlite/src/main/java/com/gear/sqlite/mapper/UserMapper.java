package com.gear.sqlite.mapper;

import com.gear.sqlite.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends JpaRepository<UserEntity, Integer> {

}
