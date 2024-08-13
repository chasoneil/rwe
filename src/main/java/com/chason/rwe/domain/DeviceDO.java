package com.chason.rwe.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端设备信息
 *
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */
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

    /**
     * 设置：主键
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }
    /**
     * 获取：主键
     */
    public String getDevId() {
        return devId;
    }
    /**
     * 设置：设备编号
     */
    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }
    /**
     * 获取：设备编号
     */
    public String getDevCode() {
        return devCode;
    }
    /**
     * 设置：设备序列号
     */
    public void setDevNumber(String devNumber) {
        this.devNumber = devNumber;
    }
    /**
     * 获取：设备序列号
     */
    public String getDevNumber() {
        return devNumber;
    }
    /**
     * 设置：分组号
     */
    public void setDevGroupCode(String devGroupCode) {
        this.devGroupCode = devGroupCode;
    }
    /**
     * 获取：分组号
     */
    public String getDevGroupCode() {
        return devGroupCode;
    }
    /**
     * 设置：设备类型
     */
    public void setDevType(String devType) {
        this.devType = devType;
    }
    /**
     * 获取：设备类型
     */
    public String getDevType() {
        return devType;
    }
    /**
     * 设置：注册人
     */
    public void setDevRegBy(String devRegBy) {
        this.devRegBy = devRegBy;
    }
    /**
     * 获取：注册人
     */
    public String getDevRegBy() {
        return devRegBy;
    }
    /**
     * 设置：注册时间
     */
    public void setDevRegTime(Date devRegTime) {
        this.devRegTime = devRegTime;
    }
    /**
     * 获取：注册时间
     */
    public Date getDevRegTime() {
        return devRegTime;
    }
    /**
     * 设置：最近在线时间
     */
    public void setDevOnlineLastTime(Date devOnlineLastTime) {
        this.devOnlineLastTime = devOnlineLastTime;
    }
    /**
     * 获取：最近在线时间
     */
    public Date getDevOnlineLastTime() {
        return devOnlineLastTime;
    }
    /**
     * 设置：状态
     */
    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }
    /**
     * 获取：状态
     */
    public String getDevStatus() {
        return devStatus;
    }

    /**
     * 设置：通电方式
     */
    public String getDevSwitchMode()
    {
        return devSwitchMode;
    }
    /**
     * 获取：通电方式
     */
    public void setDevSwitchMode(String devSwitchMode)
    {
        this.devSwitchMode = devSwitchMode;
    }

    /**
     * 设置：策略
     */
    public String getDevPolicy()
    {
        return devPolicy;
    }
    /**
     * 获取：策略
     */
    public void setDevPolicy(String devPolicy)
    {
        this.devPolicy = devPolicy;
    }
    public Date getDevSwitchHandTime()
    {
        return devSwitchHandTime;
    }
    public void setDevSwitchHandTime(Date devSwitchHandTime)
    {
        this.devSwitchHandTime = devSwitchHandTime;
    }

    public void setDevRandom(String devRandom)
    {
        this.devRandom = devRandom;
    }

    public String getDevRandom()
    {
        return devRandom;
    }

}
