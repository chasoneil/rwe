package com.chason.system.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String username;

    private String name;

    private String password;

    private Long deptId;
}
