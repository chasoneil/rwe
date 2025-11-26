package com.chason.system.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleMenuDO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;
	private Long  roleId;
	private Long menuId;
}
