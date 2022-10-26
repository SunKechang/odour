package com.bjfu.li.odour.article.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class Article {
    @TableId(value = "pk", type = IdType.AUTO)
    private int pk;

    @NotNull
    private String name;
    private String filepath;
    @NotNull
    private MultipartFile file;
}
