package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.ItemEntity;
import com.gear.sqlite.mapper.ItemMapper;
import com.gear.sqlite.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService {

    private final ItemMapper itemMapper;

    @Override
    public ItemEntity create(ItemEntity item) {
        return itemMapper.save(item);
    }

    @Override
    public List<ItemEntity> list() {
        return itemMapper.findAll();
    }

    @Override
    public ItemEntity update(ItemEntity item) {
        return itemMapper.save(item);
    }

    @Override
    public void delete(Integer id) {
        itemMapper.deleteById(id);
    }

    @Override
    public List<ItemEntity> listByIds(List<Integer> itemIds) {
        return itemMapper.findAll((root,query,builder)->{
            CriteriaBuilder.In<Object> in = builder.in(root.get("itemId"));
            itemIds.forEach(in::value);
            return query.where(in).getRestriction();
        });
    }
}
