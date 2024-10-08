package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
import com.gear.sqlite.db.ItemEntity;
import com.gear.sqlite.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final IItemService itemService;

    @GetMapping
    public Result<List<ItemEntity>> listByIds(List<Integer> itemIds) {
        List<ItemEntity> itemEntities = itemService.listByIds(itemIds);
        return Result.success(itemEntities);
    }

    @GetMapping("/list")
    public Result<List<ItemEntity>> list() {
        List<ItemEntity> list = itemService.list();
        return Result.success(list);
    }

    @PostMapping("/create")
    public Result<ItemEntity> create(@RequestBody ItemEntity item) {
        ItemEntity itemEntity = itemService.create(item);
        return Result.success(itemEntity);
    }

    @PostMapping("/update")
    public Result<ItemEntity> update(@RequestBody ItemEntity item) {
        ItemEntity update = itemService.update(item);
        return Result.success(update);
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        itemService.delete(id);
    }

}
