package com.bjfu.li.odour.review.po;

import lombok.Data;

@Data
public class User {
    private String userEmail;
    private String userPassword;
    private String name;
    private int role;   //1-上传员 2-审核员
}
