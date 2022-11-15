package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.OdourDescription;
import com.bjfu.li.odour.po.ProductOdourDescription;
import com.bjfu.li.odour.po.ProductOdourThreshold;

import java.util.List;


public interface ProductOdourDescriptionMapper extends BaseMapper<ProductOdourDescription> {

    List<ProductOdourDescription> selectByCompoundId(Integer compoundId, Integer productId);
}
