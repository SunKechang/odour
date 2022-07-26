package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.vo.SearchVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-14
 */
public interface CompoundMapper extends BaseMapper<Compound> {
    @Select("select * from compound " +
            "where is_deleted=0 " +
            "order by update_time desc " +
            "limit 5")
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
}
