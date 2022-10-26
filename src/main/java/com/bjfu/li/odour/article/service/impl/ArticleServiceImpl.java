package com.bjfu.li.odour.article.service.impl;

import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.mapper.ArticleMapper;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.article.service.ArticleService;
import com.bjfu.li.odour.utils.UUIDUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    ArticleMapper articleMapper;

    @Value("${articleDir}")
    private String articleDir;

    @Override
    public void add(Article article) throws IOException {
        String uuid = UUIDUtils.getUUID();
        String originFile = article.getFile().getOriginalFilename();
        assert originFile != null;
        String fileSuffix = originFile.substring(originFile.lastIndexOf("."));
        File file = new File(articleDir, uuid+fileSuffix);
        byte[] data = article.getFile().getBytes();
        FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
        article.setFilepath(file.getAbsolutePath());
        articleMapper.add(article);
    }

    @Override
    public void updateFile(UpdateForm form) throws IOException {
        String uuid = UUIDUtils.getUUID();
        String originFile = form.getFile().getOriginalFilename();
        assert originFile != null;
        String fileSuffix = originFile.substring(originFile.lastIndexOf("."));
        File file = new File(articleDir, uuid+fileSuffix);
        byte[] data = form.getFile().getBytes();
        FileOutputStream out = new FileOutputStream(file);
        out.write(data);
        out.close();
        form.setOldFilePath(articleMapper.getByPk(form.getPk()).getFilepath());
        form.setFilePath(file.getAbsolutePath());
        articleMapper.updateByPk(form);
        File file1 = new File(form.getOldFilePath());
        if (file1.exists()) {
            file1.delete();
        }
    }

    @Override
    public List<Article> searchByName(String name) {
        return articleMapper.searchByName(name);
    }
}
