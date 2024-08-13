package com.chason.rwe.value;

/**
 * 设备策略信息
 * 用于保存在设备dev_policy中
 * */
public class DevicePolicyValue
{
    private boolean timingFlag;
    /**
     * 定时开关标志
     * true:开
     * false:关
     * */
    public boolean isTimingFlag()
    {
        return timingFlag;
    }
    public void setTimingFlag(boolean timingFlag)
    {
        this.timingFlag = timingFlag;
    }

    private String timingOpenTime;
    /**
     * 定时开时间，HH:mm
     * */
    public String getTimingOpenTime()
    {
        return timingOpenTime;
    }
    public void setTimingOpenTime(String timingOpenTime)
    {
        this.timingOpenTime = timingOpenTime;
    }

    private String timingCloseTime;
    /**
     * 定时关时间，HH:mm
     * */
    public String getTimingCloseTime()
    {
        return timingCloseTime;
    }
    public void setTimingCloseTime(String timingCloseTime)
    {
        this.timingCloseTime = timingCloseTime;
    }

    private boolean delayedFlag;
    /**
     * 延时关闭标志
     * true:开
     * false:关
     * */
    public boolean isDelayedFlag()
    {
        return delayedFlag;
    }
    public void setDelayedFlag(boolean delayedFlag)
    {
        this.delayedFlag = delayedFlag;
    }

    private String delayedOpenTime;
    /**
     * 延时时间：HH
     * */
    public String getDelayedOpenTime()
    {
        return delayedOpenTime;
    }
    public void setDelayedOpenTime(String delayedOpenTime)
    {
        this.delayedOpenTime = delayedOpenTime;
    }
}
