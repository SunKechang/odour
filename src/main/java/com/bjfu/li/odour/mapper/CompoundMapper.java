package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.vo.SearchVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-14
 */
public interface CompoundMapper extends BaseMapper<Compound> {
    List<Compound> selectNewsList();
    List<Compound> selectAll();
    Compound selectOne(@Param("id") int id);
    List<Compound> dynamicSelect(List<SearchVo> searchVoList);
    List<Compound> selectByOdourDescription(@Param("description") String description);
    List<Compound> selectByOdourThreshold(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByRi(@Param("low") int low,@Param("high") int high);
    List<Compound> selectByNri(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByMeasured(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByLowMeasured(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByProduct(@Param(("product")) String product);
    List<Compound> advancedSearch(@Param("map") Map<String, String> properties);
}
