package com.bjfu.li.odour.base.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Base {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String base;

    private String imgPath;

    private Integer kind;

    private Integer importance;
}
