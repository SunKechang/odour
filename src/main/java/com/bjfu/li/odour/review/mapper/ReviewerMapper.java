package com.bjfu.li.odour.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.review.po.User;
import com.bjfu.li.odour.review.vo.ReviewerVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReviewerMapper extends BaseMapper<User> {

    List<ReviewerVo> getReviewers(String name);

    @Select("select name from user where userEmail = #{email}")
    String getNameByEmail(@Param("email") String email);
}
