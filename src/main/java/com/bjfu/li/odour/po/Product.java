package com.bjfu.li.odour.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 产品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    /**
     * 自增长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品描述
     */
    private String productDescription;
    /**
     * 照片
     */
    private String productPicture;
    /**
     * 产品风味描述
     */
    @TableField(exist = false)
    private List<ProductOdourDescription> odList;
    /**
     * 产品风味阈值
     */
    @TableField(exist = false)
    private List<ProductOdourThreshold> otList;
}
