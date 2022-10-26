package com.bjfu.li.odour.article.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UpdateForm {

    @NotNull
    private int pk;

    @NotNull
    private MultipartFile file;

    private String oldFilePath;

    private String filePath;
}
