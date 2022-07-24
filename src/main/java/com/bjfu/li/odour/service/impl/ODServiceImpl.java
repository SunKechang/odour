package com.bjfu.li.odour.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.mapper.OdourDescriptionMapper;
import com.bjfu.li.odour.po.OdourDescription;
import com.bjfu.li.odour.service.IODService;
import org.springframework.stereotype.Service;


@Service
public class ODServiceImpl extends ServiceImpl<OdourDescriptionMapper, OdourDescription> implements IODService {

}
