package com.bjfu.li.odour.base.controller;

import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.base.form.PictForm;
import com.bjfu.li.odour.base.po.Base;
import com.bjfu.li.odour.base.service.BaseService;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/base")
public class BaseController {

    @Resource
    BaseService baseService;

    @GetMapping("/all_available")
    public SverResponse<List<Base>> allAvailable() {
        List<Base> res = baseService.selectAllAvailable();
        return SverResponse.createRespBySuccess(res);
    }

    @GetMapping("/all")
    public SverResponse<PageInfo> all(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageInfo pageInfo;
        try {
            Page<Base> page = PageHelper.startPage(pageNum,pageSize);
            List<Base> list = baseService.selectAll();
            pageInfo = new PageInfo(list, 1);
        } finally {
            PageHelper.clearPage();
        }

        return SverResponse.createRespBySuccess(pageInfo);
    }

    @PostMapping("/add_pict")
    public SverResponse<String> addPict(@RequestBody PictForm pictForm){
        baseService.addPict(pictForm);
        return SverResponse.createRespBySuccess();
    }
}
