package com.chason.rwe.domain;

import java.io.Serializable;

/**
 * 策略信息
 * @author chason
 */
public class PolicyDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//主键
	private String policyId;
	//策略名
	private String policyName;
	//策略类型
	private String policyType;
	//策略对应的星期(多选)
	private String policyWeek;
	//策略开始时间
	private String policyStartTime;
	//策略持续时间(分钟)
	private String policyDur;
	//策略对应的设备key
	private String policySpaceKeys;


    public String getPolicyId()
    {
        return policyId;
    }
    public void setPolicyId(String policyId)
    {
        this.policyId = policyId;
    }
    public String getPolicyName()
    {
        return policyName;
    }
    public void setPolicyName(String policyName)
    {
        this.policyName = policyName;
    }
    public String getPolicyType()
    {
        return policyType;
    }
    public void setPolicyType(String policyType)
    {
        this.policyType = policyType;
    }
    public String getPolicyWeek()
    {
        return policyWeek;
    }
    public void setPolicyWeek(String policyWeek)
    {
        this.policyWeek = policyWeek;
    }
    public String getPolicyStartTime()
    {
        return policyStartTime;
    }
    public void setPolicyStartTime(String policyStartTime)
    {
        this.policyStartTime = policyStartTime;
    }
    public String getPolicyDur()
    {
        return policyDur;
    }
    public void setPolicyDur(String policyDur)
    {
        this.policyDur = policyDur;
    }
    public String getPolicySpaceKeys()
    {
        return policySpaceKeys;
    }
    public void setPolicySpaceKeys(String policySpaceKeys)
    {
        this.policySpaceKeys = policySpaceKeys;
    }
}
