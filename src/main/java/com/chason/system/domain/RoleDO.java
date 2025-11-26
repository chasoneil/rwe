package com.chason.system.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RoleDO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Long userIdCreate;
	private Date gmtCreate;
	private Date gmtModified;
	private List<Long> menuIds;
}
