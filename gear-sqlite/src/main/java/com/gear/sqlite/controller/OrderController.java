package com.gear.sqlite.controller;

import com.gear.sqlite.config.Result;
import com.gear.sqlite.db.OrderEntity;
import com.gear.sqlite.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/create")
    public Result<OrderEntity> create(@RequestBody List<Integer> itemIds) {
        OrderEntity orderEntity = orderService.create(itemIds);
        return Result.success(orderEntity);
    }

    @PostMapping("/delete")
    public void delete(Integer id) {
        orderService.delete(id);
    }

    @GetMapping("/list")
    public Result<List<OrderEntity>> list() {
        List<OrderEntity> list = orderService.list();
        return Result.success(list);
    }

}
