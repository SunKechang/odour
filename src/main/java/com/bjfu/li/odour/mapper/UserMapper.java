package com.bjfu.li.odour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfu.li.odour.po.Admin;
import com.bjfu.li.odour.po.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select userEmail from user where userEmail = #{userEmail}")
    public String selectUserEmail(String userEmail);

    @Select("select userPassword from user where userEmail = #{userEmail}")
    public String selectUserPassword(String userEmail);

    @Select("select * from user where userEmail = #{userEmail}")
    public User getByEmail(String userEmail);

    @Insert("insert into user(userEmail, userPassword, name) values(#{userEmail}, #{userPassword}, #{name})")
    public void addUser(String userEmail, String userPassword, String name);

}

