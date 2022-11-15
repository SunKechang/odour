package com.bjfu.li.odour.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 产品香气描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductOdourDescription implements Serializable {
    /**
     * 产品风味描述 id
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
     * 描述内容
     */
    private String odourDescription;
    /**
     * 风味描述来源
     */
    private String articleId;

    @TableField(exist = false)
    private String articleName;
}
