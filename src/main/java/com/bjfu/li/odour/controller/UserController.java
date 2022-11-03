package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.form.UserRoleForm;
import com.bjfu.li.odour.form.UserSearchForm;
import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.service.impl.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

//    @ResponseBody
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String register(@RequestBody User user) {
//        return userService.addUser(user);
//    }
    @PostMapping("/register")
    public String register(@RequestParam String userEmail,
                           @RequestParam String userPassword, @RequestParam String name) {
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        user.setName(name);
        return userService.addUser(user);
    }

    //    @ResponseBody
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String login(@RequestBody User user) {
//        System.out.println(user.getUserPassword());
//        return userService.selectUserEmail(user);
//    }
    @PostMapping("/login")
    public SverResponse<String> login(@RequestParam String userEmail,
                                       @RequestParam String userPassword, HttpServletResponse response) {
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        return SverResponse.createRespBySuccess(userService.selectUserEmail(user, response));
    }

    @GetMapping("/get_users")
    public SverResponse<PageInfo> getUsers(@RequestParam int pageNum, @RequestParam int pageSize,
                                           @RequestParam String email, @RequestParam String name,
                                           @RequestParam int role) {
        UserSearchForm form = new UserSearchForm();
        form.setEmail(email);
        form.setName(name);
        form.setRole(role);
        PageInfo pageInfo;
        try {
            Page<User> page = PageHelper.startPage(pageNum,pageSize);
            List<User> list = userService.getUsers(form);
            pageInfo = new PageInfo(list, 1);
        } finally {
            PageHelper.clearPage();
        }
        return SverResponse.createRespBySuccess(pageInfo);
    }

    @PostMapping("set_role")
    public SverResponse<String> setRole(@RequestBody UserRoleForm form) {
        userService.setRole(form);
        return SverResponse.createRespBySuccess();
    }
}