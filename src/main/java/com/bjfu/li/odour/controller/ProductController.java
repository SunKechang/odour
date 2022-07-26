package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.service.IProductService;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.SearchVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    IProductService productService;
    @GetMapping("/list")
    public SverResponse<PageResult> getProductList(
            @RequestParam(defaultValue = "1", value = "page") Integer page,
            @RequestParam(defaultValue = "10",value = "size") Integer size
    ){
        SearchVo searchVo = new SearchVo();
        searchVo.setPage(page);
        searchVo.setSize(size);
        PageResult products = productService.getList(searchVo);
        return SverResponse.createRespBySuccess(products);
    }

    @GetMapping("/all")
    public SverResponse<List<Product>> getProductList(){
        List<Product> products = productService.getAll();
        return SverResponse.createRespBySuccess(products);
    }
}
