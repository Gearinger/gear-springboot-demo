package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.BasketEntity;
import com.gear.sqlite.mapper.BasketMapper;
import com.gear.sqlite.service.IBasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements IBasketService {

    private final BasketMapper basketMapper;

    @Override
    public BasketEntity create(BasketEntity basket) {
        return basketMapper.save(basket);
    }

    @Override
    public BasketEntity update(BasketEntity basket) {
        return basketMapper.save(basket);
    }

    @Override
    public List<BasketEntity> list() {
        return basketMapper.findAll();
    }

    @Override
    public void delete(Integer id) {
        basketMapper.deleteById(id);
    }
}
