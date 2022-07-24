package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.OdourThresholdMapper;
import com.bjfu.li.odour.po.OdourThreshold;
import com.bjfu.li.odour.service.IOTService;
import org.springframework.stereotype.Service;


@Service
public class OTServiceImpl extends ServiceImpl<OdourThresholdMapper, OdourThreshold> implements IOTService {

}
