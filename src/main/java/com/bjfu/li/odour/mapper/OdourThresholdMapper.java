package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.OdourThreshold;

import java.util.List;


public interface OdourThresholdMapper extends BaseMapper<OdourThreshold> {

    List<OdourThreshold> selectByCompoundId(Integer id);

    List<String> getThresholdBase();

    List<String> getFunctionBase();
}
