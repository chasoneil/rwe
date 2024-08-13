package com.chason.rwe.domain;

import java.io.Serializable;
/**
 * 白名单
 */
public class WhiteListDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private String whId;

    //名称
    private String whName;

    //开始日期
    private java.sql.Date whStartDate;

    //结束日期
    private java.sql.Date whFinishDate;

    public void setWhId(String whId)
    {
        this.whId = whId;
    }

    public String getWhId()
    {
        return whId;
    }

    public void setWhName(String whName)
    {
        this.whName = whName;
    }

    public String getWhName()
    {
        return whName;
    }

    public java.sql.Date getWhStartDate()
    {
        return whStartDate;
    }

    public void setWhStartDate(java.sql.Date whStartDate)
    {
        this.whStartDate = whStartDate;
    }

    public java.sql.Date getWhFinishDate()
    {
        return whFinishDate;
    }

    public void setWhFinishDate(java.sql.Date whFinishDate)
    {
        this.whFinishDate = whFinishDate;
    }
}
