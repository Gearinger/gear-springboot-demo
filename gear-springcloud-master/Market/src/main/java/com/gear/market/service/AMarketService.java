package com.gear.market.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("Vegetable")
@Service
public interface AMarketService {

    @RequestMapping("/vegetables/potato")
    public String potato();

}
