package com.chason.rwe.domain;

import lombok.Data;

import java.util.Date;

/**
 * 交易信息
 * 交易账单 该账单是标准账单，从各大交易平台导入的信息
 */
@Data
public class TradeDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 交易时间
     */
    private Date tradeTime;

    /**
     * 交易类型 / 交易分类
     */
    private String tradeType;

    /**
     * 交易对方
     */
    private String tradeObj;

    /**
     * 对方账号
     */
    private String objAccount;

    /**
     * 商品说明
     */
    private String product;

    /**
     * 收支
     */
    private String inOut;

    /**
     * 金额
     */
    private double amount;

    /**
     * 收/付款方式
     */
    private String payType;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 商家订单号
     */
    private String sellerOrderId;

    /**
     * 交易平台
     */
    private String platform;

    /**
     * 交易备注
     */
    private String tradeComment;

    private int categoryId;

    /**
     * 创建用户ID
     */
    private Long createUserId;

    private int checked;

}
