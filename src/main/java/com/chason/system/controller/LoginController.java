package com.chason.system.controller;

import com.chason.common.utils.StringUtils;
import com.chason.system.domain.UserDO;
import com.chason.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.domain.FileDO;
import com.chason.common.domain.Tree;
import com.chason.common.service.FileService;
import com.chason.common.utils.MD5Utils;
import com.chason.common.utils.R;
import com.chason.common.utils.ShiroUtils;
import com.chason.system.domain.MenuDO;
import com.chason.system.service.MenuService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class LoginController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping({ "/", "" })
    String welcome(Model model) {
        return "redirect:/login";
    }

    @Log("请求访问主页")
    @GetMapping({ "/index_v5" })
    String index(Model model) {
//        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
//        model.addAttribute("menus", menus);
//        model.addAttribute("name", getUser().getName());
//        FileDO fileDO = fileService.get(getUser().getPicId());
//        if (fileDO != null && fileDO.getUrl() != null) {
//            if (fileService.isExist(fileDO.getUrl())) {
//                model.addAttribute("picUrl", fileDO.getUrl());
//            } else {
//                model.addAttribute("picUrl", "/img/photo_s.jpg");
//            }
//        }
//        else {
//            model.addAttribute("picUrl", "/img/photo_s.jpg");
//        }
//        model.addAttribute("username", getUser().getUsername());
//        model.addAttribute("role", this.getUser().getName());
        return "index_v5";
    }

    @Log("请求访问后台主页")
    @GetMapping({ "/index" })
    String indexV1(Model model) {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.get(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null) {
            if (fileService.isExist(fileDO.getUrl())) {
                model.addAttribute("picUrl", fileDO.getUrl());
            } else {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        }
        else {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());
        model.addAttribute("role", this.getUser().getName());
        return "index_v1";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/register")
    String reg() {
        return "register";
    }

    @Log("注册")
    @PostMapping("/register/doRegister")
    @ResponseBody
    R ajaxReg(String username, String pwd, String confirmPwd) {

        if (StringUtils.isEmpty(username)) {
            return R.error("用户名不能为空");
        }

        if (StringUtils.isEmpty(pwd)) {
            return R.error("密码不能为空");
        }

        if (!pwd.equals(confirmPwd)) {
            return R.error("两次密码输入不一致");
        }

        if (!checkPwdStrength(pwd)) {
            return R.error("密码长度必须大于等于6位，且包含大小写字母和数字三种字符");
        }

        UserDO user = userService.getByName(username);
        if (user != null) {
            return R.error("用户名已存在");
        }

        try {
            UserDO userDO = new UserDO();
            userDO.setUsername(username);
            userDO.setName(username);
            userDO.setPassword(MD5Utils.encrypt(username, pwd));
            userDO.setDeptId(20L);
            userDO.setStatus(1);
            userDO.setDeptName("注册用户");
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(52L);
            userDO.setRoleIds(roleIds);
            userDO.setGmtCreate(new Date());
            userService.save(userDO);
            log.info("register user:{} success", username);
        } catch (Exception e) {
            log.warn("register user:{} fail", username);
            log.warn(e.getMessage());
            return R.error("注册失败");
        }
        return R.ok();
    }

    private boolean checkPwdStrength(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";
        // 使用正则表达式规范密码强度
        return password.matches(passwordPattern);
    }

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password) {
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return R.ok();
        } catch (AuthenticationException e) {
            return R.error("用户或密码错误");
        }
    }

    @GetMapping("/logout")
    String logout() {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String mainPage() {
        return "main";
    }

    @GetMapping("/403")
    String error403() {
        return "403";
    }
}
