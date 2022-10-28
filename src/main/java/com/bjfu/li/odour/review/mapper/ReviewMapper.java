package com.bjfu.li.odour.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.review.controller.ReviewController;
import com.bjfu.li.odour.review.po.Review;
import com.bjfu.li.odour.review.vo.ReviewVo;

import java.util.List;

public interface ReviewMapper extends BaseMapper<Review> {

    List<ReviewVo> getReviewList(int comId);
}
