package com.bjfu.li.odour.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Compound implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String compoundName;

    private String synonym;

    private String casNo;

    private String chemicalStructure;
    private String massSpectrogram;
    private String massSpectrogramNist;


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
    private LocalDateTime createTime;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;

}
