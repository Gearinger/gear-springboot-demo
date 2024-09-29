package com.gear.sqlite.db;

import com.gear.sqlite.common.IntegerListConverter;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "backet")
public class BasketEntity extends BaseEntity {

    @Id
    private Integer basketId;

    private String basketName;

    private Integer userId;

    private String username;

    @Convert(converter = IntegerListConverter.class)
    private List<Integer> itemIds;

}
