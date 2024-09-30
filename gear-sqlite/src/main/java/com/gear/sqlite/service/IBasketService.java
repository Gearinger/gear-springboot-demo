package com.gear.sqlite.service;

import com.gear.sqlite.db.BasketEntity;

import java.util.List;

public interface IBasketService {

    BasketEntity create(BasketEntity basket);

    BasketEntity update(BasketEntity basket);

    List<BasketEntity> list();

    void delete(Integer id);
}
