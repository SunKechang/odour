package com.bjfu.li.odour.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.base.po.Base;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BaseDao extends BaseMapper<Base> {

    @Select("SELECT * FROM base WHERE img_path IS NOT NULL AND kind=1 ORDER BY importance DESC")
    List<Base> selectAllAvailable();

    @Select("SELECT * FROM base")
    List<Base> selectAll();

    @Select("SELECT * FROM base WHERE base=#{baseName}")
    List<Base> selectByName(@Param("baseName") String baseName);

    @Update("UPDATE base SET importance = importance+1 where id=#{id}")
    void incrByOne(@Param("id") Integer id);
}
