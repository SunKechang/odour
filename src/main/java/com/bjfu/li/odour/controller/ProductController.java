package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.service.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    IProductService productService;
    @GetMapping("/all")
    public SverResponse<List<Product>> getProducts(){
        List<Product> products = productService.list();
        return SverResponse.createRespBySuccess(products);
    }
}
