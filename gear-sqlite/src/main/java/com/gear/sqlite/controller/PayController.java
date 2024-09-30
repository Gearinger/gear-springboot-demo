package com.gear.sqlite.controller;

import com.gear.sqlite.db.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pay")
public class PayController {

    @PostMapping("/pay")
    public PaymentEntity pay(Integer orderId) {
        return null;
    }

    @PostMapping("/payFinishHandler")
    public void payFinishHandler(Integer orderId) {
    }

}
