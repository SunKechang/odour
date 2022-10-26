package com.bjfu.li.odour.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.po.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    void add(@Param("article") Article article);

    Article getByPk(@Param("pk") Integer pk);

    void updateByPk(@Param("form") UpdateForm form);

    List<Article> searchByName(@Param("name") String name);
}
