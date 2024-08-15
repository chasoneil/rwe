package com.chason.rwe.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chason
 */
@Data
public class SpaceDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String spaceId;
	//
	private String spaceParentId;
	//
	private String spaceCode;
	//
	private String spaceAddress;
	//
	private String spaceManagerBy;

}
