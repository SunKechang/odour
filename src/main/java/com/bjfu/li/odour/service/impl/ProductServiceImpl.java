package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.ProductKeyMapper;
import com.bjfu.li.odour.mapper.ProductMapper;
import com.bjfu.li.odour.mapper.ProductOdourDescriptionMapper;
import com.bjfu.li.odour.mapper.ProductOdourThresholdMapper;
import com.bjfu.li.odour.po.Product;
import com.bjfu.li.odour.po.ProductKey;
import com.bjfu.li.odour.po.ProductOdourDescription;
import com.bjfu.li.odour.po.ProductOdourThreshold;
import com.bjfu.li.odour.service.IProductService;
import com.bjfu.li.odour.utils.Base64Utils;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.utils.PageUtil;
import com.bjfu.li.odour.vo.SearchVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Resource
    ProductMapper productMapper;
    @Resource
    ProductKeyMapper productKeyMapper;
    @Resource
    ProductOdourThresholdMapper productOtMapper;
    @Resource
    ProductOdourDescriptionMapper productOdMapper;
    @Value("${localImgPath}")
    String localImgPath;
    @Value("${networkImgPath}")
    String networkImgPath;


    @Override
    public PageResult getList(SearchVo searchVo) {
        System.out.println(searchVo);
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        List<Product> compoundList = productMapper.selectAll();
        PageInfo<Product> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public List<Product> getAll() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> getNews(Integer num) {
        return productMapper.selectTop(num);
    }

    @Override
    public Product getOne(Integer id) {
        return productMapper.selectById(id);
    }

    @Override
    public boolean delete(Integer id) {
        // product
        Product product = productMapper.selectById(id);
        QueryWrapper<ProductKey> keyQueryWrapper=new QueryWrapper<>();
        keyQueryWrapper.eq("product_id",product.getId());
        productKeyMapper.delete(keyQueryWrapper);
        QueryWrapper<ProductOdourDescription> productOdQueryWrapper=new QueryWrapper<>();
        productOdQueryWrapper.eq("product_id",product.getId());
        productOdMapper.delete(productOdQueryWrapper);
        QueryWrapper<ProductOdourThreshold> productOtQueryWrapper=new QueryWrapper<>();
        productOtQueryWrapper.eq("product_id",product.getId());
        productOtMapper.delete(productOtQueryWrapper);
        productMapper.deleteById(id);
        return false;
    }

    @Override
    public boolean save(Product product) {
        String productPicture = product.getProductPicture();
        try {
            // 存图片
            if (productPicture != null && !productPicture.equals("")) {
                productPicture = networkImgPath + "product picture/" + Base64Utils.generateImage(productPicture, localImgPath + "product picture");
                product.setProductPicture(productPicture);
            }
            productMapper.insert(product);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
