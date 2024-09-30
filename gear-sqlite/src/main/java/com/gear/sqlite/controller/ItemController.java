package com.gear.sqlite.controller;

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
    public List<ItemEntity> listByIds(List<Integer> itemIds) {
        return itemService.listByIds(itemIds);
    }

    @GetMapping("/list")
    public List<ItemEntity> list() {
        return itemService.list();
    }

    @PostMapping("/create")
    public ItemEntity create(@RequestBody ItemEntity item) {
        return itemService.create(item);
    }

    @PostMapping("/update")
    public ItemEntity update(@RequestBody ItemEntity item) {
        return itemService.update(item);
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        itemService.delete(id);
    }

}
