package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.OdourIntensityFunction;

import java.util.List;

public interface OdourIntensityFunctionMapper extends BaseMapper<OdourIntensityFunction> {

    List<OdourIntensityFunction> selectByCompoundId(Integer id);
}
