package com.gear.sqlite.service;

import com.gear.sqlite.db.ItemEntity;

import java.util.List;

public interface IItemService {
    ItemEntity add(ItemEntity item);

    List<ItemEntity> listAll();
}
