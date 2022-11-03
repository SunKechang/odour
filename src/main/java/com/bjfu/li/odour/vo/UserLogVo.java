package com.bjfu.li.odour.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class UserLogVo {

    private String operation;

    private String email;

    private String name;

    private String ip;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}
