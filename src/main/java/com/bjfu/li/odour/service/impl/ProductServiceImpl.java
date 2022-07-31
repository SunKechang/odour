package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.*;
import com.bjfu.li.odour.po.*;
import com.bjfu.li.odour.service.IProductService;
import com.bjfu.li.odour.utils.Base64Utils;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.utils.PageUtil;
import com.bjfu.li.odour.vo.SearchVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    CompoundMapper compoundMapper;
    @Resource
    ProductOdourDescriptionMapper productOdMapper;
    @Value("${localImgPath}")
    String localImgPath;
    @Value("${networkImgPath}")
    String networkImgPath;


    @Override
    public PageResult searchList(SearchVo searchVo) {
        String propertyDescription = searchVo.getSearchValue().toString().trim();
        String property = searchVo.getSearchProperty();
        searchVo.setSearchRule("like");
        searchVo.setSearchValue(propertyDescription);
        searchVo.setSearchProperty(property);
        List<SearchVo> searchVoList = new ArrayList<>();
        searchVoList.add(searchVo);
        List<Product> productList = this.dynamicSelect(searchVoList);
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        PageInfo<Product> pageInfo = new PageInfo<>(productList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public PageResult searchCompound(SearchVo searchVo) {
        String propertyDescription = searchVo.getSearchValue().toString().trim();
        String property = searchVo.getSearchProperty();
        List<Compound> compoundList;
        switch (property) {
            case "odour_description":
                compoundList = productMapper.selectCompoundByOdourDescription(
                        searchVo.getProductId(),
                        propertyDescription
                );
                break;
            case "odour_threshold":
                int odourThreshold = Integer.parseInt(propertyDescription);
                compoundList = productMapper.selectCompoundByOdourThreshold(
                        searchVo.getProductId(),
                        odourThreshold - 10,
                        odourThreshold + 10
                );
                break;
            case "compound_ri":
                int ri = Integer.parseInt(propertyDescription);
                compoundList = productMapper.selectCompoundByRi(
                        searchVo.getProductId(),
                        ri - 100,
                        ri + 100
                );
                break;
            case "compound_nri":
                int nri= Integer.parseInt(propertyDescription);
                compoundList = productMapper.selectCompoundByNri(
                        searchVo.getProductId(),
                        nri-100,
                        nri+100
                );
                break;
            case "measured":
                double measured= Double.parseDouble(propertyDescription);
                compoundList = productMapper.selectCompoundByMeasured(
                        searchVo.getProductId(),
                        measured-0.05,
                        measured+0.05
                );
                break;
            case "lowmeasured":
                double lowmeasured= Double.parseDouble(propertyDescription);
                compoundList = productMapper.selectCompoundByLowMeasured(
                        searchVo.getProductId(),
                        lowmeasured-0.05,
                        lowmeasured+0.05
                );
                break;
            default:
                searchVo.setSearchRule("like");
                searchVo.setSearchValue(propertyDescription);
                searchVo.setSearchProperty(property);
                compoundList = productMapper.selectCompound(searchVo);
        }
        PageHelper.startPage(searchVo.getPage(), searchVo.getSize());
        PageInfo<Compound> pageInfo = new PageInfo<>(compoundList);
        return PageUtil.getPageResult(pageInfo);
    }

    @Override
    public List<Product> dynamicSelect(List<SearchVo> searchVoList) {
        //为了测试连表和前后端字段名不一致，可省略
        for (SearchVo searchVo : searchVoList) {
            if (StringUtils.equalsIgnoreCase("id", searchVo.getSearchProperty())) {
                searchVo.setSearchProperty("product.id");
            } else if (StringUtils.equalsIgnoreCase("product_name", searchVo.getSearchProperty())) {
                searchVo.setSearchProperty("product.product_name");
            }
        }
        return productMapper.dynamicSelect(searchVoList);
    }

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
        Product product = productMapper.selectById(id);
        QueryWrapper<ProductKey> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("product_id", product.getId());
        List<ProductKey> productKeyList = productKeyMapper.selectList(keyQueryWrapper);
        List<Compound> compoundList = new ArrayList<>();
        for (ProductKey productKey : productKeyList) {
            QueryWrapper<Compound> compoundQueryWrapper = new QueryWrapper<>();
            compoundQueryWrapper.eq("id", productKey.getCompoundId());
            compoundList.addAll(compoundMapper.selectList(compoundQueryWrapper));
            QueryWrapper<ProductOdourDescription> productOdQueryWrapper = new QueryWrapper<>();
            productOdQueryWrapper.eq("product_id", product.getId());
            productOdMapper.selectList(productOdQueryWrapper);
        }
        product.setCompoundList(compoundList);
        return product;
    }

    @Override
    public boolean delete(Integer id) {
        // product
        Product product = productMapper.selectById(id);
        QueryWrapper<ProductKey> keyQueryWrapper = new QueryWrapper<>();
        keyQueryWrapper.eq("product_id", product.getId());
        productKeyMapper.delete(keyQueryWrapper);
        QueryWrapper<ProductOdourDescription> productOdQueryWrapper = new QueryWrapper<>();
        productOdQueryWrapper.eq("product_id", product.getId());
        productOdMapper.delete(productOdQueryWrapper);
        QueryWrapper<ProductOdourThreshold> productOtQueryWrapper = new QueryWrapper<>();
        productOtQueryWrapper.eq("product_id", product.getId());
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        String productPicture = product.getProductPicture() == null ? "" : product.getProductPicture();
        Product _product = new Product();
        if (productPicture.startsWith("data")) {
            _product = productMapper.selectById(product.getId());
        }
        try {
            // 上传了 productPicture 且是一个有效的base64
            if (!productPicture.equals("") & productPicture.startsWith("data")) {
                // 数据库中该数据存在
                assert _product != null;
                // 文件地址存在且能够争取解析
                if (_product.getProductPicture() != null) {
                    if (_product.getProductPicture().lastIndexOf("/") != -1) {
                        // 文件名
                        String productPictureFileName = _product.getProductPicture().substring(_product.getProductPicture().lastIndexOf("/"));
                        File oldPictureFileName = new File(localImgPath + "product picture/" + productPictureFileName);
                        // 删除原文件成功
                        assert !oldPictureFileName.exists() || oldPictureFileName.delete();
                    }
                }
                // 保存新的文件并更新字段
                productPicture = networkImgPath + "product picture/" + Base64Utils.generateImage(productPicture, localImgPath + "product picture/");
                product.setProductPicture(productPicture);
            }
            product.setUpdateTime(LocalDateTime.now());
            productMapper.updateById(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
