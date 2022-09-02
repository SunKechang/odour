package com.bjfu.li.odour.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品中香气阈值
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductOdourThreshold implements Serializable {
    /**
     * 阈值Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品Id
     */
    private Integer productId;
    /**
     * 化合物Id
     */
    private Integer compoundId;
    /**
     * 阈值
     */
    private BigDecimal odourThreshold;
    /**
     * 阈值来源
     */
    private String odourThresholdReference;
    /**
     * Base
     */
    private String odourThresholdBase;
}
