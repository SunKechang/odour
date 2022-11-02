package com.bjfu.li.odour.article.service;

import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.po.Article;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ArticleService {
    int add(Article article) throws IOException;

    void updateFile(UpdateForm form) throws IOException;

    List<Article> searchByName(String name);

    void getFile(HttpServletResponse response, int pk);
}
