package com.bjfu.li.odour.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OdourIntensityFunction implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String functionImg;

    private String odourBase;

    private String articleId;

    @TableField(exist = false)
    private String articleName;

    private Integer compoundId;
}
