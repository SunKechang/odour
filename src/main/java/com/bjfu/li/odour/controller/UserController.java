package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.form.RegisterForm;
import com.bjfu.li.odour.form.UserPassForm;
import com.bjfu.li.odour.form.UserRoleForm;
import com.bjfu.li.odour.form.UserSearchForm;
import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.service.impl.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @PostMapping("/register")
    public String register(@RequestBody @Validated RegisterForm form) {
        User user = new User();
        user.setUserEmail(form.getUserEmail());
        user.setUserPassword(form.getUserPassword());
        user.setName(form.getName());
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
    public SverResponse<String> setRole(@RequestBody @Validated UserRoleForm form) {
        userService.setRole(form);
        return SverResponse.createRespBySuccess();
    }

    @PostMapping("send_email/{email}")
    public SverResponse<String> sendEmail(@PathVariable String email) {
        System.out.println(email);
        userService.sendEmail(mailSender, email);
        return SverResponse.createRespBySuccess();
    }

    @PostMapping("/update/password")
    public SverResponse<Integer> updatePassword(@RequestBody UserPassForm form) {
        int res = userService.updatePassword(form);
        return SverResponse.createRespBySuccess(res);
    }
}