package com.chason.rwe.enums;

/**
 * 交易平台枚举类
 * @author Chason
 */
public enum TradePlatform {

    ALIPAY("alipay", "支付宝"),
    WECHAT("wechat", "微信"),
    ICBC("icbc", "工商银行"),
    CMB("cmb", "招商银行"),
    BC("bc", "中国银行"),
    ABC("abc", "农业银行"),
    CCB("ccb", "建设银行"),
    PSBC("psbc", "交通银行"),
    PINGAN("pingan", "平安银行"),
    UNKNOWN("unknown", "未知");

    private String code;

    private String name;

    TradePlatform(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        // 根据code获取name
        for (TradePlatform platform : TradePlatform.values()) {
            if (platform.getCode().equals(code)) {
                return platform.getName();
            }
        }
        return UNKNOWN.getName();
    }

}
