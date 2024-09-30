package com.gear.sqlite.controller;

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
    public BasketEntity create(@RequestBody BasketEntity basket) {
        return basketService.create(basket);
    }

    @PostMapping("/update")
    public BasketEntity update(@RequestBody BasketEntity basket) {
        return basketService.update(basket);
    }

    @GetMapping("/list")
    public List<BasketEntity> list() {
        return basketService.list();
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        basketService.delete(id);
    }

}
