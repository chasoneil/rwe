package com.chason.rwe.enums;

import lombok.Getter;

public enum WordTypeEnum {

    N("n", "名词"),
    V("v", "动词"),
    ADJ("adj", "形容词"),
    AUX("aux", "助词");

    @Getter
    private String sign;

    @Getter
    private String name;

    WordTypeEnum(String sign, String name) {
        this.sign = sign;
        this.name = name;
    }

    public static String getNameFromSign(String sign) {
        for (WordTypeEnum w: WordTypeEnum.values()) {
            if (w.sign.equalsIgnoreCase(sign)) {
                return w.name;
            }
        }
        return null;
    }

}
