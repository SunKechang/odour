package com.bjfu.li.odour.article.service.impl;

import com.bjfu.li.odour.article.form.UpdateForm;
import com.bjfu.li.odour.article.mapper.ArticleMapper;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.article.service.ArticleService;
import com.bjfu.li.odour.utils.FileUtils;
import com.bjfu.li.odour.utils.UUIDUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    ArticleMapper articleMapper;

    @Value("${articleDir}")
    private String articleDir;

    @Override
    public int add(Article article) throws IOException {
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
        articleMapper.insert(article);
        return article.getPk();
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

    @Override
    public void getFile(HttpServletResponse response, int pk) {
        Article article = articleMapper.getByPk(pk);
        String filePath = article.getFilepath();
        try {
            System.out.println(filePath);
            FileInputStream inputStream = new FileInputStream(filePath);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String diskfilename = FileUtils.getNameFromPath(filePath);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");
            System.out.println("data.length " + data.length);
            response.setContentLength(data.length);
            response.setHeader("Content-Range", "" + (data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            OutputStream os = response.getOutputStream();

            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean judgeFileName(String name) {
        List<Article> articleList = articleMapper.getByName(name);
        if(articleList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
