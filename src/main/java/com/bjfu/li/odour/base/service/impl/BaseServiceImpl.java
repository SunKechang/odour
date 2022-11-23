package com.bjfu.li.odour.base.service.impl;

import com.bjfu.li.odour.base.form.PictForm;
import com.bjfu.li.odour.base.mapper.BaseDao;
import com.bjfu.li.odour.base.po.Base;
import com.bjfu.li.odour.base.service.BaseService;
import com.bjfu.li.odour.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BaseServiceImpl implements BaseService {

    @Resource
    BaseDao baseDao;

    @Value("${networkImgPath}")
    String networkImgPath;

    @Value("${localImgPath}")
    String localImgPath;

    @Override
    public List<Base> selectAllAvailable() {
        return baseDao.selectAllAvailable();
    }

    @Override
    public List<Base> selectAll() {
        return baseDao.selectAll();
    }

    @Override
    public void addPict(PictForm pictForm) {
        String image = pictForm.getImage();
        if(image != null && !image.equals("")) {
            image = networkImgPath + "base/" + Base64Utils.generateImage(image, localImgPath+"base");
            Base base = new Base();
            base.setId(pictForm.getId());
            base.setImgPath(image);
            baseDao.updateById(base);
        }
    }
}
