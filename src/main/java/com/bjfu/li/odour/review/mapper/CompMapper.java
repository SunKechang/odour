package com.bjfu.li.odour.review.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.review.form.StatusForm;
import com.bjfu.li.odour.review.vo.CommitVo;
import com.bjfu.li.odour.review.vo.Unreview;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CompMapper extends BaseMapper<Compound> {

    List<Unreview> getUnreviewed(String email);

    List<Unreview> getReviewed(String email);

    List<CommitVo> getCommitted(String email);
    List<CommitVo> getMyReviewed(String email);

    void setStatusById(@Param("form")StatusForm form);
}
