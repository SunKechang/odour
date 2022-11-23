package com.bjfu.li.odour.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class SearchVo {
    private String searchProperty;
    private String searchRule;
    private Object searchValue;
    private Integer page;
    private Integer size;
    private Integer productId;

    private Integer searchKind; // 0-全局搜索 1-threshold搜索 2-function搜索
    private String base;    //要搜索的基质

    @Override
    public String toString() {
        return "SearchVo{" +
                "searchProperty='" + searchProperty + '\'' +
                ", searchRule='" + searchRule + '\'' +
                ", searchValue='" + searchValue + '\'' +
                '}';
    }
}
