package com.bjfu.li.odour.admin.service;

import com.bjfu.li.odour.admin.form.RoleForm;
import com.bjfu.li.odour.admin.vo.UserView;
import com.bjfu.li.odour.po.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface IAdminService extends IService<Admin> {
    Integer loginCheck(String account,String password) throws UnsupportedEncodingException;
    boolean isDuplicated(String account);
    List<UserView> getUserList(String name, Integer role);
    void setRole(RoleForm form);
}
