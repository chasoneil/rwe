package com.chason.rwe.enums;

/**
 * 收入类型枚举类
 * @author Chason
 */
public enum IncomeEnum {

    /**
     * 工资	奖金  补贴
     * 红包	投资  兼职
     * 退款	还款  报销
     * 返现	其他
     */
    SALARY("工资"),
    BONUS("奖金"),
    SUBSIDY("补贴"),
    RED_PACKET("红包"),
    INVESTMENT("投资"),
    PART_TIME("兼职"),
    REFUND("退款"),
    REPAYMENT("还款"),
    REIMBURSEMENT("报销"),
    RETURN("返现"),
    OTHER("其他");

    private String name;

    IncomeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
