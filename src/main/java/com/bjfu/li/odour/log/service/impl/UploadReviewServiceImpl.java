package com.bjfu.li.odour.log.service.impl;

import com.bjfu.li.odour.log.mapper.UploadReviewLogMapper;
import com.bjfu.li.odour.log.po.LogUploadReview;
import com.bjfu.li.odour.log.service.UploadReviewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UploadReviewServiceImpl implements UploadReviewService {

    @Resource
    UploadReviewLogMapper mapper;

    @Override
    public void insertLog(LogUploadReview logOperation) {
        mapper.insert(logOperation);
    }
}
