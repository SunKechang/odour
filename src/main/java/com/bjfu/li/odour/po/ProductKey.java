package com.bjfu.li.odour.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 关系表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductKey {
    /**
     * 自增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 化合物 Id
     */
    private Integer compoundId;
    /**
     * 产品 Id
     */
    private Integer productId;

}
