package com.gear.sqlite.mapper;

import com.gear.sqlite.db.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMapper extends JpaRepository<ItemEntity, Integer> {
}
