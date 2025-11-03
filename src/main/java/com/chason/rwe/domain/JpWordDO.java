package com.chason.rwe.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author chason
 * Japanese word
 */
@Data
public class JpWordDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    // 假名
    private String word;

    // 日文单词对应的中文
    private String wordCn;

    // 词性
    private String wordType;

    // 发音，音型，多个之间用;隔开
    private String wordVoice;

    // 含义（多个含义中间用;隔开）
    private String zhMean;

    // 所属课程
    private Integer lessonId;

    // 学习次数
    private int learnTime;

    /*
     * 0:未学习 1:学习中 2:已掌握
     */
    private int learned;

    // 上次复习时间
    private Date lastReviewTime;

    // 创建时间
    private Date createTime;

}
