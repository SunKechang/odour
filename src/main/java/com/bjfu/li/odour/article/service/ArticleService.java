package com.bjfu.li.odour.article.service;

import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.po.Article;

import java.io.IOException;
import java.util.List;

public interface ArticleService {
    void add(Article article) throws IOException;

    void updateFile(UpdateForm form) throws IOException;

    List<Article> searchByName(String name);
}
