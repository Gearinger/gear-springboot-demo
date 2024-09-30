package com.gear.sqlite.controller;

import com.gear.sqlite.db.OrderEntity;
import com.gear.sqlite.db.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/create")
    public OrderEntity create(List<Integer> itemIds){
        return null;
    }

    @PostMapping("/delete")
    public void delete(Integer id){

    }

    @GetMapping("/list")
    public List<OrderEntity> list(){
        return null;
    }

}
