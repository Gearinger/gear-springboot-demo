package com.gear.sqlite.service.impl;

import com.gear.sqlite.db.ItemEntity;
import com.gear.sqlite.db.OrderEntity;
import com.gear.sqlite.mapper.OrderMapper;
import com.gear.sqlite.service.IItemService;
import com.gear.sqlite.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderMapper orderMapper;

    private final IItemService itemService;

    @Override
    public OrderEntity create(List<Integer> itemIds) {
        List<ItemEntity> itemEntities = itemService.listByIds(itemIds);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItems(itemEntities);
        orderEntity.setAmount(itemEntities.stream().map(ItemEntity::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        orderEntity.setPayment(BigDecimal.ZERO);
        return orderMapper.save(orderEntity);
    }

    @Override
    public void delete(Integer id) {
        orderMapper.deleteById(id);
    }

    @Override
    public List<OrderEntity> list() {
        return orderMapper.findAll();
    }
}
