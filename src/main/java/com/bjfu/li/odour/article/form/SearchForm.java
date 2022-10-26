package com.bjfu.li.odour.article.form;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SearchForm {

    @Min(1)
    private Integer pageNum;

    @Min(1)
    private Integer pageSize;

    private String name;
}
