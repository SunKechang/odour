package com.bjfu.li.odour.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.po.Log;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.service.IProductService;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.SearchVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    IProductService productService;
    @Resource
    LogServiceImpl logService;
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
    @PostMapping("/add")
    public SverResponse<String> addCompound(@RequestBody Product product, HttpServletRequest request){
        if(productService.save(product)) {
            String token= request.getHeader("Authorization");
            DecodedJWT verify= JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Create",product.getId(),adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }
    @GetMapping("/news")
    public SverResponse<List<Product>> getNews(){
        List<Product> products = productService.getAll();
        return SverResponse.createRespBySuccess(products);
    }
    /**
     * 获取单条产品信息
     */
    @GetMapping("/{id}")
    public SverResponse<Product> getOne(
            @PathVariable Integer id
    ){
        Product product = productService.getOne(id);
        return SverResponse.createRespBySuccess(product);
    }
    @DeleteMapping("/{id}")
    public SverResponse<String> deleteController(
            @PathVariable Integer id, HttpServletRequest request
    ) {
        if(productService.delete(id)){
            String token= request.getHeader("Authorization");
            DecodedJWT verify=JWTUtils.verify(token);
            Integer adminId=Integer.valueOf(verify.getClaim("id").asString());
            Log log=new Log(null,"Delete",id,adminId, LocalDateTime.now());
            logService.save(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }
}
