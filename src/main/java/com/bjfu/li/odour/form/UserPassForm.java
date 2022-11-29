package com.bjfu.li.odour.form;

import lombok.Data;

@Data
public class UserPassForm {

    private String email;

    private String password;

    //验证码
    private String code;
}
