package com.bjfu.li.odour.review.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bjfu.li.odour.po.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Compound {

    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 化合物名称
     */
    private String compoundName;
    /**
     * 俗名
     */
    private String synonym;
    /**
     * CAS号
     */
    private String casNo;
    /**
     * 分子结构图
     */
    private String chemicalStructure;
    /**
     * 质谱
     */
    private String massSpectrogram;

    private String massSpectrogramNist;

    /**
     * 索引文献的主键
     */
    private String article;

    @TableField(exist = false)
    private List<OdourDescription> odList;

    @TableField(exist = false)
    private List<OdourThreshold> otList;

    @TableField(exist = false)
    private List<Ri> riList;

    @TableField(exist = false)
    private List<Nri> nriList;

    @TableField(exist = false)
    private List<Measured> mrList;

    @TableField(exist = false)
    private List<LowMeasured> lowmrList;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime = LocalDateTime.now();

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime = LocalDateTime.now();

    @TableLogic
    private Integer isDeleted=2;    //0-已检阅，通过 1-已删除 2-待检阅 3-已检阅，不通过
    /**
     * 上传人
     */
    @TableField("uploader")
    private String uploader;
    /**
     * 审核人
     */
    @TableField("reviewer")
    private String reviewer;
    /**
     * 包含此化合物的产品
     */
    @TableField(exist = false)
    private List<Product> productList;
}
