package com.chason.rwe.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chason
 * English word pojo
 */
@Data
public class WordDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String word;

    private String wordType;

    private String lesson;

    private String zhMean;

    private int learnTime;

    // 1 means learned 0 means not
    private int learned;

    private Date lastReviewTime;

    private Date createTime;

}
