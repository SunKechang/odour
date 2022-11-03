package com.bjfu.li.odour.controller;


import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.form.UserLogForm;
import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.service.impl.LogServiceImpl;
import com.bjfu.li.odour.vo.LogVo;
import com.bjfu.li.odour.vo.UserLogVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Resource
    LogServiceImpl logService;

    /**
     *
     * @return 所有操作记录
     */
    @GetMapping("/all")
    public SverResponse<List<LogVo>> getLogs(){
        List<LogVo> logs=logService.getLogList();
        return SverResponse.createRespBySuccess(logs);
    }

    @GetMapping("/user")
    public SverResponse<PageInfo> getUserLogs(@RequestParam int pageNum, @RequestParam int pageSize,
                                              @RequestParam String operation, @RequestParam String email){
        UserLogForm form = new UserLogForm();
        form.setEmail(email);
        form.setOperation(operation);
        PageInfo pageInfo;
        try {
            Page<UserLogVo> page = PageHelper.startPage(pageNum,pageSize);
            List<UserLogVo> list = logService.getUserLogList(form);
            pageInfo = new PageInfo(list, 1);
        } finally {
            PageHelper.clearPage();
        }
        return SverResponse.createRespBySuccess(pageInfo);
    }

}
