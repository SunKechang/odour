package com.bjfu.li.odour.article.controller;

import com.bjfu.li.odour.article.form.SearchForm;
import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.article.service.ArticleService;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文献控制器
 * @author SunKechang
 * @since 2022-10-26
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @PostMapping("/add")
    public SverResponse<Integer> add(Article article){
        int pk = -1;
        try {
            pk = articleService.add(article);
        } catch (IOException exception) {
            return SverResponse.createByErrorCodeMessage(502, "file save error");
        }
        return SverResponse.createRespBySuccess(pk);
    }

    @PostMapping("/update")
    public SverResponse<String> update(UpdateForm form){
        try {
            articleService.updateFile(form);
        } catch (IOException exception) {
            return SverResponse.createByErrorCodeMessage(502, "file update error");
        }
        return SverResponse.createRespBySuccess();
    }

    @GetMapping("/search")
    public SverResponse<PageInfo> search(@RequestParam int pageNum, @RequestParam int pageSize,
                                         @RequestParam String name){
        PageInfo pageInfo;
        try {
            Page<Article> page = PageHelper.startPage(pageNum,pageSize);
            List<Article> list = articleService.searchByName(name);
            pageInfo = new PageInfo(list, 1);
        } finally {
            PageHelper.clearPage();
        }

        return SverResponse.createRespBySuccess(pageInfo);
    }

    @GetMapping("/getFile")
    public void getFile(HttpServletResponse response, @RequestParam int pk) {
        articleService.getFile(response, pk);
    }


}
