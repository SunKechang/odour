package com.bjfu.li.odour.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OilOdour implements Serializable {



    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String oilOdour;

    private BigDecimal oilOdourIntensity;

    private String ooNote;

    private Integer oilId;


}