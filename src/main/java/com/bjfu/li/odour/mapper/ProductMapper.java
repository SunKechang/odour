package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Product;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {
    List<Product> selectAll();
}
