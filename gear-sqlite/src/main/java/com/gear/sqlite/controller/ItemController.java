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

    @GetMapping("/listAll")
    public List<ItemEntity> listAll() {
        return itemService.listAll();
    }

    @PostMapping("/add")
    public ItemEntity add(@RequestBody ItemEntity item) {
        return itemService.add(item);
    }

}
