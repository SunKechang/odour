package com.bjfu.li.odour.form;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
public class UserRoleForm {

    @NotBlank
    private String email;

    @Range(min = 0, max = 3)
    private int role;
}
