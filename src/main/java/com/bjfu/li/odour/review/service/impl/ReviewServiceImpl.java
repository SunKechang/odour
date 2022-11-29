package com.bjfu.li.odour.review.service.impl;

import com.bjfu.li.odour.review.form.ApproveForm;
import com.bjfu.li.odour.review.form.StatusForm;
import com.bjfu.li.odour.review.mapper.CompMapper;
import com.bjfu.li.odour.review.mapper.ReviewMapper;
import com.bjfu.li.odour.review.mapper.ReviewerMapper;
import com.bjfu.li.odour.review.po.Review;
import com.bjfu.li.odour.review.service.ReviewService;
import com.bjfu.li.odour.review.vo.CommitVo;
import com.bjfu.li.odour.review.vo.ReviewVo;
import com.bjfu.li.odour.review.vo.ReviewerVo;
import com.bjfu.li.odour.review.vo.Unreview;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    CompMapper compMapper;

    @Resource
    ReviewMapper reviewMapper;

    @Resource
    ReviewerMapper reviewerMapper;

    @Override
    public List<Unreview> getUnreviewed(String email) {
        return compMapper.getUnreviewed(email);
    }

    @Override
    public List<Unreview> getReviewed(String email) {
        return compMapper.getReviewed(email);
    }

    @Override
    public List<Unreview> getAll(String email) {
        return compMapper.getAll(email);
    }

    @Override
    public List<CommitVo> getCommitted(String email) {
        return compMapper.getCommitted(email);
    }

    @Override
    public List<CommitVo> getMyReviewed(String email) {
        return compMapper.getMyReviewed(email);
    }

    @Override
    public void setStatus(StatusForm form) {
        compMapper.setStatusById(form);
    }

    @Override
    public void approve(ApproveForm form, String email) {
        StatusForm form1  = new StatusForm();
        form1.setId(form.getComId());
        form1.setStatus(form.getStatus());
        compMapper.setStatusById(form1);
        Review review = new Review();
        review.setReviewerId(email);
        review.setComment(form.getComment());
        review.setComId(form.getComId());
        reviewMapper.insert(review);
    }

    @Override
    public List<ReviewVo> getApprovalList(int comId) {
        return reviewMapper.getReviewList(comId);
    }

    @Override
    public List<ReviewerVo> getReviewers(String name, String userName) {
        return reviewerMapper.getReviewers(name, userName);
    }
}