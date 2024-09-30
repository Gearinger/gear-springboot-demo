package com.gear.sqlite.service;

import com.gear.sqlite.db.ItemEntity;

import java.util.List;

public interface IItemService {
    ItemEntity create(ItemEntity item);

    List<ItemEntity> list();

    ItemEntity update(ItemEntity item);

    void delete(Integer id);

    List<ItemEntity> listByIds(List<Integer> itemIds);
}
