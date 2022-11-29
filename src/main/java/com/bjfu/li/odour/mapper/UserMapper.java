package com.bjfu.li.odour.mapper;

import com.bjfu.li.odour.form.UserRoleForm;
import com.bjfu.li.odour.form.UserSearchForm;
import com.bjfu.li.odour.po.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    List<User> getUsers(@Param("form") UserSearchForm form);

    void setRole(@Param("form") UserRoleForm form);

    @Update("UPDATE user SET userPassword = #{password} WHERE userEmail = #{email}")
    void updateByEmail(@Param("email") String email, @Param("password") String password);

}

