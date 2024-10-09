package com.gear.sqlite.service;

import com.gear.sqlite.db.OrderEntity;

import java.util.List;

public interface IOrderService {
    OrderEntity create(List<Integer> itemIds);

    void delete(Integer id);

    List<OrderEntity> list();
}
