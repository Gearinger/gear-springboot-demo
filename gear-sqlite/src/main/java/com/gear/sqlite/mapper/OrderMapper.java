package com.gear.sqlite.mapper;

import com.gear.sqlite.db.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends JpaRepository<OrderEntity, Integer> {
}
