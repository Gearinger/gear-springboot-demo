package com.gear.sqlite.db;

import com.gear.sqlite.common.IntegerListConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "backet")
public class BasketEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer basketId;

    private String basketName;

    private Integer userId;

    private String username;

    @Convert(converter = IntegerListConverter.class)
    private List<Integer> itemIds;

}
