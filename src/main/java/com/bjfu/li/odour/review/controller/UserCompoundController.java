package com.bjfu.li.odour.review.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bjfu.li.odour.common.log.LogAop;
import com.bjfu.li.odour.common.log.LogParameter;
import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.log.po.LogUploadReview;
import com.bjfu.li.odour.log.service.UploadReviewService;
import com.bjfu.li.odour.po.Compound;
import com.bjfu.li.odour.po.Log;
import com.bjfu.li.odour.service.ICompoundService;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/user/compound")
public class UserCompoundController {

    @Resource
    ICompoundService compoundService;

    @Resource
    UploadReviewService reviewService;

    @Resource
    UploadReviewService uploadReviewService;

    @PostMapping("/update")
    public SverResponse<String> updateCompound(@RequestBody Compound compound, HttpServletRequest request){
        compound.setIsDeleted(2);
        compoundService.update(compound);
        String token= request.getHeader("Authorization");
        DecodedJWT verify=JWTUtils.verify(token);
        String email = verify.getClaim("email").asString();
        LogUploadReview log = new LogUploadReview();
        log.setOperation("UPDATE "+compound.getId());
        log.setEmail(email);
        log.setIp(request.getRemoteAddr());
        uploadReviewService.insertLog(log);
        return SverResponse.createRespBySuccess();
    }

    @PostMapping("/add")
    public SverResponse<String> addCompound(@RequestBody Compound compound, HttpServletRequest request){
        String token= request.getHeader("Authorization");
        DecodedJWT verify=JWTUtils.verify(token);
        compound.setIsDeleted(2);
        compound.setUploader(verify.getClaim("email").asString());
        if(compoundService.save(compound)) {
            LogUploadReview log = new LogUploadReview();
            log.setOperation("ADD "+compound.getId());
            log.setEmail(verify.getClaim("email").asString());
            log.setIp(request.getRemoteAddr());
            reviewService.insertLog(log);
            return SverResponse.createRespBySuccess();
        }else
            return SverResponse.createRespByError();
    }
}
