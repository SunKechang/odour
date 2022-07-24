package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Oil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.Map;
import java.util.List;

public interface OilMapper extends BaseMapper<Oil> {

    Oil selectOne(@Param("id") int id);

    @Select("select * from oil " +
            "where is_deleted=0 " +
            "limit 5")
    List<Oil> selectNewsList();

    List<Oil> selectAll();

    List<Oil> selectByOilName(@Param("name") String name);

    List<Oil> selectByOilType(@Param("type") String type);

    List<Oil> selectByOilBrand(@Param("brand") String brand);

    List<Oil> selectByCas(@Param("cas") String cas);

    List<Oil> selectByOilOdour(@Param("description") String description);
}
