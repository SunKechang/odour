package com.bjfu.li.odour.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.review.po.User;
import com.bjfu.li.odour.review.vo.ReviewerVo;

import java.util.List;

public interface ReviewerMapper extends BaseMapper<User> {

    List<ReviewerVo> getReviewers(String name);
}
