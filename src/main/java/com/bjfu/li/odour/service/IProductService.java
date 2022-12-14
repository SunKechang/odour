package com.bjfu.li.odour.service;

import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.SearchVo;

import java.util.List;

public interface IProductService {
    PageResult searchList(SearchVo searchVo);
    PageResult searchCompound(SearchVo searchVo);
    PageResult getList(SearchVo searchVo);
    List<Product> getAll();
    List<Product> getNews(Integer num);
    Product getOne(Integer id);
    boolean delete(Integer id);
    boolean save(Product product);
    boolean update(Product product);
    List<Product> dynamicSelect(List<SearchVo> searchVoList);
}
