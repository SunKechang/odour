package com.bjfu.li.odour.review.vo;

import lombok.Data;

@Data
public class Unreview {

    private int id;

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

    private String article;
}
