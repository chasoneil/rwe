package com.chason.rwe.enums;

/**
 * 交易数据来源
 * @author Chason
 */

public enum TradeDataSourceEnum {

    UNKNOWN("未知来源"),

    ALIPAY("支付宝"),

    WECHAT("微信");

    private String desc;

    TradeDataSourceEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
