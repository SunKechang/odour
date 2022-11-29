package com.bjfu.li.odour.review.service;

import com.bjfu.li.odour.review.form.ApproveForm;
import com.bjfu.li.odour.review.form.StatusForm;
import com.bjfu.li.odour.review.vo.CommitVo;
import com.bjfu.li.odour.review.vo.ReviewVo;
import com.bjfu.li.odour.review.vo.ReviewerVo;
import com.bjfu.li.odour.review.vo.Unreview;

import java.util.List;

public interface ReviewService {

    List<Unreview> getUnreviewed(String email);

    List<Unreview> getReviewed(String email);

    List<Unreview> getAll(String email);

    List<CommitVo> getCommitted(String email);

    List<CommitVo> getMyReviewed(String email);

    void setStatus(StatusForm form);

    void approve(ApproveForm form, String email);

    List<ReviewVo> getApprovalList(int comId);

    List<ReviewerVo> getReviewers(String name, String userName);
}
