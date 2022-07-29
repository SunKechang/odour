package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.ProductMapper;
import com.bjfu.li.odour.po.Product;
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
