package com.bjfu.li.odour.review.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ApproveForm {

    private int comId;

    private String comment;

    @Range(min = 0, max = 4)
    private int status;
}
