package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.OdourThreshold;
import com.bjfu.li.odour.po.ProductOdourDescription;
import com.bjfu.li.odour.po.ProductOdourThreshold;

import java.util.List;


public interface ProductOdourThresholdMapper extends BaseMapper<ProductOdourThreshold> {

    List<ProductOdourThreshold> selectByCompoundId(Integer compoundId, Integer productId);
}
