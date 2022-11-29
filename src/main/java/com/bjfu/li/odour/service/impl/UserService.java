package com.bjfu.li.odour.service.impl;

import com.bjfu.li.odour.common.token.JWTUtils;
import com.bjfu.li.odour.form.UserPassForm;
import com.bjfu.li.odour.form.UserRoleForm;
import com.bjfu.li.odour.form.UserSearchForm;
import com.bjfu.li.odour.mapper.UserMapper;
import com.bjfu.li.odour.po.User;
import com.bjfu.li.odour.utils.MailUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String sender;

    Cache<String, String> localCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5, TimeUnit.MINUTES).build();

    public String selectUserEmail(User user, HttpServletResponse response) {
        String userEmail = user.getUserEmail();
        String userPassword = user.getUserPassword();
//        System.out.println(userEmail);
//        System.out.println(userPassword);

        String result;

        // 将输入的密码使用md5加密
        String passwordMD5 = passwordMD5(userEmail, userPassword);
        User dbUser = userMapper.getByEmail(userEmail);
        // 用户不存在
        if (dbUser == null) {
            result = "0";
            //  用户存在，但密码输入错误
        }else if(!dbUser.getUserPassword().equals(passwordMD5)){
            result = "1";
            //  登录成功
        }else {
            Map<String, String> map = new HashMap<>();
            map.put("role", "" + dbUser.getRole());
            map.put("id", "-1");
            map.put("account", dbUser.getName());
            map.put("email", dbUser.getUserEmail());
            String jwt = JWTUtils.getToken(map);
            response.setHeader("Authorization", jwt);
            result = jwt;
        }
        return result;
    }

    public String addUser(User user) {
        String userEmail = user.getUserEmail();
        // 用户存在
        if(userMapper.selectUserEmail(userEmail) != null)
            return "0";
        String userPassword = user.getUserPassword();
        String passwordMD5 = passwordMD5(userEmail, userPassword);
        userMapper.addUser(userEmail, passwordMD5, user.getName());
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

    public List<User> getUsers(UserSearchForm form) {
        return userMapper.getUsers(form);
    }

    public void setRole(UserRoleForm form) {
        userMapper.setRole(form);
    }

    /**
     * 发送验证码到指定邮箱
     *
     * @param mailSender spring自带
     * @param receiver   接受地址
     */
    public void sendEmail(JavaMailSenderImpl mailSender, String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");//设置邮件标题
        String code = MailUtils.generateVerCode();
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + code + ",本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");    //设置邮件正文
        message.setFrom(sender);//发件人
        message.setTo(receiver);//收件人
        mailSender.send(message);//发送邮件

        localCache.put(code, receiver);
    }

    /**
     * 用户修改密码
     */
    public int updatePassword(UserPassForm form) {
        String cacheCode = localCache.getIfPresent(form.getCode());
        if(cacheCode == null) {
            //验证码不存在或已过期
            return -1;
        }
        if(!cacheCode.equals(form.getEmail())) {
            //与发给本人的验证码不符
            return 0;
        }
        String passwordMD5 = passwordMD5(form.getEmail(), form.getPassword());
        userMapper.updateByEmail(form.getEmail(), passwordMD5);
        return 1;
    }

}
