package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.OdourDescription;
import com.bjfu.li.odour.po.OdourThreshold;

import java.util.List;


public interface OdourDescriptionMapper extends BaseMapper<OdourDescription> {

    List<OdourDescription> selectByCompoundId(Integer id);
}
