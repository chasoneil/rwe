package com.chason.rwe.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 策略信息
 * @author chason
 */
@Data
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
}
