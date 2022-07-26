package com.bjfu.li.odour.service;

import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.SearchVo;

import java.util.List;

public interface IProductService {
    PageResult getList(SearchVo searchVo);
    List<Product> getAll();
}
