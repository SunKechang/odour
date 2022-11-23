package com.bjfu.li.odour.base.service;

import com.bjfu.li.odour.base.form.PictForm;
import com.bjfu.li.odour.base.po.Base;

import java.util.List;

public interface BaseService {

    List<Base> selectAllAvailable();

    List<Base> selectAll();

    void addPict(PictForm pictForm);
}
