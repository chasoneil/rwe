package com.chason.rwe.page;

import lombok.Data;

import java.util.Date;

/**
 * 账单前端展示页面
 * @author Chason
 */
@Data
public class TradePage {

    private String orderId;

    private Date tradeTime;

    private String tradeType;

    private String tradeObj;

    private String objAccount;

    private String product;

    private String inOut;

    private double amount;

    private String tradeStatus;

    private String platform;

    private String tradeComment;

    private String categoryName;

    private String categoryType;

    private Long createUserId;

}
