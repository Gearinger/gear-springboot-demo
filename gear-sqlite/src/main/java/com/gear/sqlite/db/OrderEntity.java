package com.gear.sqlite.db;

import com.gear.sqlite.common.ItemEntityConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "order")
public class OrderEntity extends BaseEntity {

    @Id
    private Integer orderId;

    private String orderIdentity;

    private Integer userId;

    private String username;

    private String address;

    private String status;

    private BigDecimal payment;

    @Convert(converter = ItemEntityConverter.class)
    private List<ItemEntity> items;

}
