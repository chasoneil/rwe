package com.chason.rwe.domain;

import lombok.Data;

/**
 * 记账信息，用于记账表的持久化对象
 * @author Chason
 */
@Data
public class KeepAccountDO {

    /*
    id int primary key auto_increment comment '账单ID',
    user_id int not null comment '用户ID',
    amount double not null comment '金额',
    trade_time datetime not null comment '交易时间',
    trade_variety varchar(32) not null comment '交易类型（一级菜单）',
    trade_type varchar(32) not null comment '交易类型（二级菜单）',
    trade_statistics text not null comment '交易统计（第二维度统计）',
    pay_for varchar(32) not null comment '为谁付款',
    trade_status varchar(16) not null comment '交易状态',
    trade_comment text not null comment '交易备注',
    checked int default 0 comment '是否已检查',
     */

    private int id;
    private int userId;
    private double amount;
    private String tradeTime;
    private String tradeVariety;
    private String tradeType;
    private String tradeStatistics;
    private String payFor;
    private String tradeStatus;
    private String tradeComment;
    private int checked;
}
