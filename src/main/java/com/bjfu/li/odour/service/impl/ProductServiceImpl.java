package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.ProductMapper;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.service.IProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Resource
    ProductMapper productMapper;


    @Override
    public List<Product> list() {
        return productMapper.selectAll();
    }
}
