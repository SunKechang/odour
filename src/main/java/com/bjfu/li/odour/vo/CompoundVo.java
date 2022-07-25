package com.bjfu.li.odour.vo;

import com.bjfu.li.odour.po.Compound;
import lombok.Data;

import java.util.List;

@Data
public class CompoundVo {
    private Integer current;
    private Integer size;
    private Long total;
    private List<Compound> compoundList;
}
