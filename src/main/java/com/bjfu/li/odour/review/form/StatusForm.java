package com.bjfu.li.odour.review.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
public class StatusForm {

    private int id;

    @Range(min = 0, max = 4)
    private int status;
}
