package com.gear.sqlite.db;

import com.gear.sqlite.common.ListConverter;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "orderr")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    private String orderNo;

    private Integer userId;

    private String username;

    private String address;

    private String status;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 付款
     */
    private BigDecimal payment;

    @Convert(converter = ListConverter.class)
    private List<ItemEntity> items;

}
