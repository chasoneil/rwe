package com.chason.system.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chason.system.dao.RoleDao;
import com.chason.system.dao.RoleMenuDao;
import com.chason.system.dao.UserDao;
import com.chason.system.dao.UserRoleDao;
import com.chason.system.domain.RoleDO;
import com.chason.system.domain.RoleMenuDO;
import com.chason.system.service.RoleService;


@Service
public class RoleServiceImpl implements RoleService {

    public static final String ROLE_ALL_KEY = "\"role_all\"";

    public static final String DEMO_CACHE_NAME = "role";

    @Autowired
    private RoleDao roleMapper;

    @Autowired
    private RoleMenuDao roleMenuMapper;

    @Autowired
    private UserDao userMapper;

    @Autowired
    UserRoleDao userRoleMapper;

    @Override
    public List<RoleDO> list() {
        return roleMapper.list(new HashMap<>(16));
    }

    @Override
    public List<RoleDO> list(Long userId) {
        List<Long> rolesIds = userRoleMapper.listRoleId(userId);
        List<RoleDO> roles = roleMapper.list(new HashMap<>(16));
        for (RoleDO roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), roleId)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }
    @Transactional
    @Override
    public int save(RoleDO role) {
        role.setGmtCreate(new Date());
        int count = roleMapper.save(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenuDO> rms = new ArrayList<>();
        if(menuIds != null) {
            for (Long menuId : menuIds) {
                RoleMenuDO rmDo = new RoleMenuDO();
                rmDo.setRoleId(roleId);
                rmDo.setMenuId(menuId);
                rms.add(rmDo);
            }
        }
        roleMenuMapper.removeByRoleId(roleId);
        if (!rms.isEmpty()) {
            roleMenuMapper.batchSave(rms);
        }
        return count;
    }

    @Transactional
    @Override
    public int remove(Long id) {
        int count = roleMapper.remove(id);
        roleMenuMapper.removeByRoleId(id);
        return count;
    }

    @Override
    public RoleDO get(Long id) {
        return roleMapper.get(id);
    }

    @Transactional
    @Override
    public int batchremove(Long[] ids) {
        return roleMapper.batchRemove(ids);
    }

    @Override
    public int update(RoleDO role) {
        role.setGmtModified(new Date());
        int r = roleMapper.update(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        roleMenuMapper.removeByRoleId(roleId);
        List<RoleMenuDO> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenuDO rmDo = new RoleMenuDO();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        if (!rms.isEmpty()) {
            roleMenuMapper.batchSave(rms);
        }
        return r;
    }

    @Override
    public int getRoleLevel(Long userId) {
        int roleLevel = 20;
        List<RoleDO> roles = list(userId);
        for (RoleDO role : roles) {
            if (role.getRoleSign().equals("true")) {
                if (role.getRoleId() == 1) {
                    roleLevel = 0;
                } else if (role.getRoleId() == 49) {
                    roleLevel = 10;
                }
                break;
            }
        }
        return roleLevel;
    }

}
