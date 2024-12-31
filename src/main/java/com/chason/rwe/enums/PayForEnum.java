package com.chason.rwe.enums;

/**
 * 收益人的枚举类
 * @author Chason
 */
public enum PayForEnum {

    MYSELF("本人"),
    PARENT_1("父母"),
    PARENT_2("妻子父母"),
    PARENT_3("丈夫父母"),
    FRIEND("朋友"),
    WIFE("妻子"),
    HUSBAND("丈夫"),
    COLLEAGUE("同事"),
    CHILDREN("子女"),
    OTHER("其他");

    private String name;

    PayForEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String getName(String enumName) {
        for (PayForEnum payForEnum : PayForEnum.values()) {
            if (payForEnum.name().equalsIgnoreCase(enumName)) {
                return payForEnum.name;
            }
        }
        return null;
    }
}
