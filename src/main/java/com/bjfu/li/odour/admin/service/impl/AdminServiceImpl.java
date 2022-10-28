package com.bjfu.li.odour.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bjfu.li.odour.admin.form.RoleForm;
import com.bjfu.li.odour.admin.vo.UserView;
import com.bjfu.li.odour.mapper.UserMapper;
import com.bjfu.li.odour.po.Admin;
import com.bjfu.li.odour.admin.mapper.AdminMapper;
import com.bjfu.li.odour.admin.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.utils.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    AdminMapper adminMapper;

    @Resource
    UserMapper userMapper;

    public Integer loginCheck(String account, String password) throws UnsupportedEncodingException {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("account",account);
        Admin admin=adminMapper.selectOne(queryWrapper);
        if(admin==null)
            return null;
        if(admin.getPassword().equals(MD5Utils.MD5Encode(password, "UTF-8", false)))
            return admin.getId();
        else
            return null;
    }

    @Override
    public boolean isDuplicated(String account) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("is_deleted",0).eq("account",account);
        return adminMapper.selectOne(queryWrapper)==null;
    }

    @Override
    public List<UserView> getUserList(String name, Integer role) {
        return adminMapper.getAllUser(name, role);
    }

    @Override
    public void setRole(RoleForm form) {
       adminMapper.setRole(form);
    }
}
