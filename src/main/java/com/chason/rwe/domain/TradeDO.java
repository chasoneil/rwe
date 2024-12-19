package com.chason.rwe.domain;

import lombok.Data;

import java.util.Date;

/**
 * 账单信息
 */
@Data
public class TradeDO {

    private static final long serialVersionUID = 1L;

    private String orderId;

    private Date tradeTime;

    private String tradeType;

    private String tradeObj;

    private String objAccount;

    private String product;

    private String inOut;

    private double amount;

    private String payType;

    private String tradeStatus;

    private String sellerOrderId;

    private String tradeComment;

}
