package com.bjfu.li.odour.log.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class LogUploadReview {
    @TableId(value = "pk", type = IdType.AUTO)
    private Integer pk;

    private String operation;

    private String email;

    private String ip;

    private Timestamp createTime;
}
