package com.chason.rwe.domain;

import lombok.Data;

/**
 * for table tbl_keep_account
 * @author Chason
 */
@Data
public class KeepAccountDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private long userId;

    private double amount;

    private String tradeTime;

    private String inOut;

    private String tradeVariety;

    private String tradeType;

    private String tradePeriod;

    private String tradeStatistics;

    private String tradeDetail;

    private String payAccount;

    private String payMethod;

    private String consumer;

    private String tradeStatus;

    private String tradeComment;

    private int checked;
}
