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

    private int id;

    private String word;

    private String type;

    private int lesson;

    private String zhMean;

    private Date learnTime;

    // 1 means learned 0 means not
    private int learned;

    private Date lastViewTime;

    private Date createTime;

}
