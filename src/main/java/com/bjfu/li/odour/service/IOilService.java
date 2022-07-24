package com.bjfu.li.odour.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjfu.li.odour.po.Oil;

import java.util.List;



public interface IOilService extends IService<Oil> {
    List<Oil> searchOils (String property, String propertyDescription);
    boolean save(Oil oil);
    boolean updateById(Oil oil);
}
