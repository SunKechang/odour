package com.bjfu.li.odour.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.utils.PageResult;
import com.bjfu.li.odour.vo.BasesVo;
import com.bjfu.li.odour.vo.DownloadVo;
import com.bjfu.li.odour.vo.SearchVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface ICompoundService extends IService<Compound> {
    List<DownloadVo> searchByCasPro(String cas);

    List<Compound> advancedSearch(Map<String, String> properties);

    PageResult searchList(SearchVo searchVo);
    boolean delete(Integer id);
    List<Compound> getNews();
    boolean save(Compound compound);
    boolean update(Compound compound);
    PageResult getList(SearchVo searchVo);
    Compound getOne(Integer id);
    List<Compound> dynamicSelect(SearchVo searchVo);

    BasesVo getAllBase();
}
