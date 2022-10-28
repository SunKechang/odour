package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
                           @RequestParam String userPassword) {
        System.out.println("开始注册" + userPassword );
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        return userService.addUser(user);
    }

    //    @ResponseBody
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String login(@RequestBody User user) {
//        System.out.println(user.getUserPassword());
//        return userService.selectUserEmail(user);
//    }
    @PostMapping("/login")
    public String login(@RequestParam String userEmail,
                        @RequestParam String userPassword, HttpServletResponse response) {
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        return userService.selectUserEmail(user, response);
    }
}