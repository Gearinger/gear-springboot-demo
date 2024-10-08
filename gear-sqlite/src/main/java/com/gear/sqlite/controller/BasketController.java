package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
import com.gear.sqlite.db.BasketEntity;
import com.gear.sqlite.service.IBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/busket")
public class BasketController {

    private final IBasketService basketService;

    @PostMapping("/create")
    public Result<BasketEntity> create(@RequestBody BasketEntity basket) {
        BasketEntity basketEntity = basketService.create(basket);
        return Result.success(basketEntity);
    }

    @PostMapping("/update")
    public Result<BasketEntity> update(@RequestBody BasketEntity basket) {
        BasketEntity update = basketService.update(basket);
        return Result.success(update);
    }

    @GetMapping("/list")
    public Result<List<BasketEntity>> list() {
        List<BasketEntity> list = basketService.list();
        return Result.success(list);
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        basketService.delete(id);
    }

}
