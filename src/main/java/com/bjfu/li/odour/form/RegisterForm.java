package com.bjfu.li.odour.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RegisterForm {

    @Email
    String userEmail;

    @NotBlank
    String userPassword;

    @NotBlank
    String name;
}
