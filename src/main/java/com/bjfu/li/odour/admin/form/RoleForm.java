package com.bjfu.li.odour.admin.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class RoleForm {
    @NotBlank
    private String email;

    @Range(min = 0, max = 2)
    private Integer role;
}
