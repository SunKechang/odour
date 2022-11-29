package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.vo.DownloadVo;
import com.bjfu.li.odour.vo.SearchVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
    List<Compound> dynamicSelect(@Param("searchVo") SearchVo searchVo);
    List<Compound> selectByOdourDescription(@Param("description") String description, @Param("kind") Integer kind, @Param("base") String base);
    List<Compound> selectByOdourThreshold(@Param("low") double low,@Param("high") double high, @Param("kind") Integer kind, @Param("base") String base);
    List<Compound> selectByRi(@Param("low") int low,@Param("high") int high);
    List<Compound> selectByNri(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByMeasured(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByLowMeasured(@Param("low") double low,@Param("high") double high);
    List<Compound> selectByProduct(@Param(("product")) String product);
    List<Compound> advancedSearch(@Param("map") Map<String, String> properties);
    List<Compound> selectByCasPro(String params);
    List<DownloadVo> selectByCasUltra(@Param("cas") String cas);
    void updateOne(@Param("comp") Compound compound);

    @Update("UPDATE compound SET is_deleted=1 WHERE id=#{id}")
    void deleteOne(@Param("id") Integer id);
}
