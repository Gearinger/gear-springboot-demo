package com.gear.sqlite.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear.sqlite.common.Transform;
import com.gear.sqlite.common.TransformConverter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 物体
 *
 * @author guoyingdong
 * @date 2024/09/29
 */
@Data
@Entity
@Table(name = "item")
public class ItemEntity extends BaseEntity {

    /**
     * 物体id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    @Length(max = 255, message = "描述长度不能超过255")
    private String description;

    /**
     * 位置
     */
    @Convert(converter = TransformConverter.class)
    private Transform transform;

    /**
     * 模型url
     */
    private String modelUrl;


    /**
     * 地址
     */
    private String address;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    private String type;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

}
