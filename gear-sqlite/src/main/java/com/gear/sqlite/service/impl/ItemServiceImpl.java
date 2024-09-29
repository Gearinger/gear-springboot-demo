package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.ItemEntity;
import com.gear.sqlite.mapper.ItemMapper;
import com.gear.sqlite.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService {

    private final ItemMapper itemMapper;

    @Override
    public ItemEntity add(ItemEntity item) {
        return itemMapper.save(item);
    }

    @Override
    public List<ItemEntity> listAll() {
        return itemMapper.findAll();
    }
}
