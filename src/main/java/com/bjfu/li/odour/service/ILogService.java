package com.bjfu.li.odour.service;

import com.bjfu.li.odour.form.UserLogForm;
import com.bjfu.li.odour.po.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjfu.li.odour.vo.LogVo;
import com.bjfu.li.odour.vo.UserLogVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface ILogService extends IService<Log> {
    List<LogVo> getLogList();

    List<UserLogVo> getUserLogList(UserLogForm form);
}
