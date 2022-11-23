package com.bjfu.li.odour.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bjfu.li.odour.po.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DownloadVo {

    private Integer id;

    private String compoundName;

    private String synonym;

    private String casNo;

    private List<OdourDescription> odList;

    private List<OdourThreshold> otList;
}
