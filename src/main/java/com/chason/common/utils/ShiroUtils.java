package com.chason.common.utils;

import com.chason.system.domain.RoleDO;
import com.chason.system.service.RoleService;
import com.chason.system.service.impl.RoleServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.chason.system.domain.UserDO;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public class ShiroUtils {

    @Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }

    public static UserDO getUser() {
        Object object = getSubjct().getPrincipal();
        return (UserDO)object;
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}
