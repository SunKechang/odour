package com.bjfu.li.odour.vo;

import lombok.Data;

@Data
public class SearchVo {
    private String searchProperty;
    private String searchRule;
    private Object searchValue;
    private Integer page;
    private Integer size;

    @Override
    public String toString() {
        return "SearchVo{" +
                "searchProperty='" + searchProperty + '\'' +
                ", searchRule='" + searchRule + '\'' +
                ", searchValue='" + searchValue + '\'' +
                '}';
    }
}
