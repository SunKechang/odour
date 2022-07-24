package com.bjfu.li.odour.service.impl;

import com.bjfu.li.odour.mapper.UserMapper;
import com.bjfu.li.odour.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public String selectUserEmail(User user) {
        String userEmail = user.getUserEmail();
        String userPassword = user.getUserPassword();
//        System.out.println(userEmail);
//        System.out.println(userPassword);

        String result = "-1";

        // 将输入的密码使用md5加密
        String passwordMD5 = passwordMD5(userEmail, userPassword);

        // 用户不存在
        if (userMapper.selectUserEmail(userEmail) == null) {
            result = "0";
            return result;
            //  用户存在，但密码输入错误
        }else if(!userMapper.selectUserPassword(userEmail).equals(passwordMD5) ){
            result = "1";
            return result;
            //  登录成功
        }else if(userMapper.selectUserPassword(userEmail).equals(passwordMD5)) {
            result = "2";
            return result;
        }
        return result;
    }

    public String addUser(User user) {
        String userEmail = user.getUserEmail();
        // 用户存在
        if(userMapper.selectUserEmail(userEmail) != null)
            return "0";
        String userPassword = user.getUserPassword();
        System.out.println(userEmail + "***" + userPassword);
        String passwordMD5 = passwordMD5(userEmail, userPassword);
        userMapper.addUser(userEmail, passwordMD5);
        return "1";
    }

    // md5加密
    public String passwordMD5(String userEmail, String userPassword) {
        // 需要加密的字符串
        String src = userEmail + userPassword;
        try {
            // 加密对象，指定加密方式
            MessageDigest md5 = MessageDigest.getInstance("md5");
            // 准备要加密的数据
            byte[] b = src.getBytes();
            // 加密：MD5加密一种被广泛使用的密码散列函数，
            // 可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致
            byte[] digest = md5.digest(b);
            // 十六进制的字符
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                    '6', '7', 'A', 'B', 'C', 'd', 'o', '*', '#', '/'};
            StringBuffer sb = new StringBuffer();
            // 处理成十六进制的字符串(通常)
            // 遍历加密后的密码，将每个元素向右位移4位，然后与15进行与运算(byte变成数字)
            for (byte bb : digest) {
                sb.append(chars[(bb >> 4) & 15]);
                sb.append(chars[bb & 15]);
            }
            // 打印加密后的字符串





            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
