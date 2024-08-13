package com.chason.rwe.value;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DeviceFileValue
{
    @Excel(name = "设备号" , orderNum = "0")
    private String devCode;

    @Excel(name = "序列号" , orderNum = "1")
    private String devNumber;

    @Excel(name = "设备类型" , orderNum = "2")
    private String devType;

    @Excel(name = "分组号" , orderNum = "3")
    private String devGroupCode;

    public String getDevCode()
    {
        return devCode;
    }

    public void setDevCode(String devCode)
    {
        this.devCode = devCode;
    }

    public String getDevNumber()
    {
        return devNumber;
    }

    public void setDevNumber(String devNumber)
    {
        this.devNumber = devNumber;
    }

    public String getDevType()
    {
        return devType;
    }

    public void setDevType(String devType)
    {
        this.devType = devType;
    }

    public String getDevGroupCode()
    {
        return devGroupCode;
    }

    public void setDevGroupCode(String devGroupCode)
    {
        this.devGroupCode = devGroupCode;
    }

}
