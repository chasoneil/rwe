package com.chason.rwe.domain;

/**
 * AccountDictDO 账户字典表
 * @author Chason
 */

import lombok.Data;

@Data
public class AccountDictDO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private long userId;

    private String dictName;

    private String dictValue;

    private String dictRemark;

    private int sys;
}
