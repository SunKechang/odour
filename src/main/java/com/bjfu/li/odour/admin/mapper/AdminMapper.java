package com.bjfu.li.odour.admin.mapper;

import com.bjfu.li.odour.admin.form.RoleForm;
import com.bjfu.li.odour.admin.vo.UserView;
import com.bjfu.li.odour.po.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author li
 * @since 2020-11-06
 */
public interface AdminMapper extends BaseMapper<Admin> {
    List<UserView> getAllUser(String name, Integer role);

    void setRole(@Param("form") RoleForm form);
}
