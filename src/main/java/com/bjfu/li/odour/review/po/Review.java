package com.bjfu.li.odour.review.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.sql.rowset.spi.SyncResolver;
import java.time.LocalDateTime;

@Data
public class Review {

    @TableId(value = "pk", type = IdType.AUTO)
    private int pk;

    private int comId;

    private String comment;

    private String reviewerId;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
