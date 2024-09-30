package com.gear.sqlite.mapper;

import com.gear.sqlite.db.BasketEntity;
import com.gear.sqlite.db.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketMapper extends JpaRepository<BasketEntity, Integer> {

}
