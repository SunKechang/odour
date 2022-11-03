package com.bjfu.li.odour.review.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.article.po.Article;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.log.po.LogUploadReview;
import com.bjfu.li.odour.log.service.UploadReviewService;
import com.bjfu.li.odour.review.form.ApproveForm;
import com.bjfu.li.odour.review.form.StatusForm;
import com.bjfu.li.odour.review.service.ReviewService;
import com.bjfu.li.odour.review.vo.CommitVo;
import com.bjfu.li.odour.review.vo.ReviewVo;
import com.bjfu.li.odour.review.vo.ReviewerVo;
import com.bjfu.li.odour.review.vo.Unreview;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user/review")
public class ReviewController {

    @Resource
    ReviewService reviewService;

    @Resource
    UploadReviewService uploadReviewService;

    @GetMapping("/get_unreviewed")
    public SverResponse<PageInfo> getUnreviewed(HttpServletRequest request, @RequestParam int pageNum,
                                                      @RequestParam int pageSize) {
        String token = request.getHeader("Authorization");
        DecodedJWT verify= JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        PageInfo pageInfo;
        Page<Unreview> page = PageHelper.startPage(pageNum,pageSize);
        List<Unreview> list = reviewService.getUnreviewed(email);
        pageInfo = new PageInfo(list, 1);
        PageHelper.clearPage();
        return SverResponse.createRespBySuccess(pageInfo);
    }

    @GetMapping("/get_reviewed")
    public SverResponse<PageInfo> getReviewed(HttpServletRequest request, @RequestParam int pageNum,
                                                    @RequestParam int pageSize) {
        String token = request.getHeader("Authorization");
        DecodedJWT verify= JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        PageInfo pageInfo;
        Page<Unreview> page = PageHelper.startPage(pageNum,pageSize);
        List<Unreview> list = reviewService.getReviewed(email);
        pageInfo = new PageInfo(list, 1);
        PageHelper.clearPage();
        return SverResponse.createRespBySuccess(pageInfo);
    }

    @GetMapping("/get_committed")
    public SverResponse<PageInfo> getCommitted(HttpServletRequest request, @RequestParam int pageNum,
                                                     @RequestParam int pageSize) {
        String token = request.getHeader("Authorization");
        DecodedJWT verify= JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        PageInfo pageInfo;
        Page<Unreview> page = PageHelper.startPage(pageNum,pageSize);
        List<CommitVo> list = reviewService.getCommitted(email);
        pageInfo = new PageInfo(list, 1);
        PageHelper.clearPage();
        return SverResponse.createRespBySuccess(pageInfo);
    }

    @GetMapping("/get_my_reviewed")
    public SverResponse<PageInfo> getMyReviewed(HttpServletRequest request, @RequestParam int pageNum,
                                                      @RequestParam int pageSize) {
        String token = request.getHeader("Authorization");
        DecodedJWT verify= JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        PageInfo pageInfo;
        Page<CommitVo> page = PageHelper.startPage(pageNum,pageSize);
        List<CommitVo> list =  reviewService.getMyReviewed(email);
        pageInfo = new PageInfo(list, 1);
        PageHelper.clearPage();
        return SverResponse.createRespBySuccess(pageInfo);
    }

    @PostMapping("/set_status")
    public SverResponse<String> setStatus(@RequestBody StatusForm form, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        DecodedJWT verify= JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        reviewService.setStatus(form);
        return SverResponse.createRespBySuccess("Success");
    }

    @PostMapping("/approve")
    public SverResponse<String> approve(@RequestBody ApproveForm form, HttpServletRequest request) {
        String token= request.getHeader("Authorization");
        DecodedJWT verify=JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        LogUploadReview log = new LogUploadReview();
        log.setOperation("REVIEW "+form.getComId());
        log.setEmail(email);
        log.setIp(request.getRemoteAddr());
        uploadReviewService.insertLog(log);

        reviewService.approve(form, email);
        return SverResponse.createRespBySuccess("Success");
    }

    @GetMapping("/get_approval")
    public SverResponse<List<ReviewVo>> getApproval(@RequestParam int comId) {
        return SverResponse.createRespBySuccess(reviewService.getApprovalList(comId));
    }

    @GetMapping("/get_reviewers")
    public SverResponse<List<ReviewerVo>> getReviewers(@RequestParam String name) {
        return SverResponse.createRespBySuccess(reviewService.getReviewers(name));
    }
}
