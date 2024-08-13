package com.chason.rwe.domain;

import java.io.Serializable;

/**
 * @author chason
 */
public class SpaceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private String spaceId;
	//
	private String spaceParentId;
	//
	private String spaceCode;
	//
	private String spaceAddress;
	//
	private String spaceManagerBy;

	/**
	 * 设置：
	 */
	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	/**
	 * 获取：
	 */
	public String getSpaceId() {
		return spaceId;
	}

	public String getSpaceParentId()
    {
        return spaceParentId;
    }

    public void setSpaceParentId(String spaceParentId)
    {
        this.spaceParentId = spaceParentId;
    }
	/**
	 * 设置：
	 */
	public void setSpaceCode(String spaceCode) {
		this.spaceCode = spaceCode;
	}
	/**
	 * 获取：
	 */
	public String getSpaceCode() {
		return spaceCode;
	}
	/**
	 * 设置：
	 */
	public void setSpaceAddress(String spaceAddress) {
		this.spaceAddress = spaceAddress;
	}
	/**
	 * 获取：
	 */
	public String getSpaceAddress() {
		return spaceAddress;
	}
	/**
	 * 设置：
	 */
	public void setSpaceManagerBy(String spaceManagerBy) {
		this.spaceManagerBy = spaceManagerBy;
	}
	/**
	 * 获取：
	 */
	public String getSpaceManagerBy() {
		return spaceManagerBy;
	}



}
