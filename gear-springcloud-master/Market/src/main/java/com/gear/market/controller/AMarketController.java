package com.gear.market.controller;

import com.gear.market.dto.AMarketDTO;
import com.gear.market.service.AMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/AMarket")
public class AMarketController {

    @Qualifier("com.gear.market.service.AMarketService")
    @Autowired
    AMarketService aMarketService;

    @RequestMapping("/findAMarketList")
    public List<AMarketDTO> findAMarketList(){
        List<AMarketDTO> aMarketDTOS = new ArrayList<>();
        aMarketDTOS.add(new AMarketDTO());
        return aMarketDTOS;
    }

    @RequestMapping("/potato")
    public String potato(){
        return aMarketService.potato();
    }
}
