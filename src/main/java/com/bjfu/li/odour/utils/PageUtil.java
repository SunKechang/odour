package com.bjfu.li.odour.utils;


import com.github.pagehelper.PageInfo;

public class PageUtil {

    /**
     * 将分页信息封装到统一的接口
     * @return PageResult
     */
    public static PageResult getPageResult( PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}
