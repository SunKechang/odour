package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.ProductMapper;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.service.IProductService;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.utils.PageUtil;
import com.bjfu.li.odour.vo.SearchVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Resource
    ProductMapper productMapper;


    @Override
    public PageResult getList(SearchVo searchVo) {
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        List<Product> compoundList = productMapper.selectAll();
        PageInfo<Product> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public List<Product> getAll() {
        return productMapper.selectAll();
    }
}
