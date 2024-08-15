package com.chason.rwe.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端设备信息
 */
@Data
public class DeviceDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private String devId;
    //设备编号
    private String devCode;
    //设备序列号（mac地址）
    private String devNumber;
    //分组号
    private String devGroupCode;
    //设备类型
    private String devType;
    //注册人
    private String devRegBy;
    //注册时间
    private Date devRegTime;
    //最近在线时间
    private Date devOnlineLastTime;
    //̬状态
    private String devStatus;
    //通电方式
    private String devSwitchMode;
    //策略信息
    private String devPolicy;
    //设备通电方式变为手动的时间
    private Date devSwitchHandTime;
    //是否是随机策略
    private String devRandom;
}
