package com.chason.system.controller;

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

import java.util.List;

@Controller
public class LoginController extends BaseController
{

    @Autowired
    private MenuService menuService;

    @Autowired
    private FileService fileService;

    @GetMapping({ "/", "" })
    String welcome(Model model)
    {
        return "redirect:/login";
    }

    @Log("请求访问主页")
    @GetMapping({ "/index" })
    String index(Model model)
    {
        List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
        model.addAttribute("menus", menus);
        model.addAttribute("name", getUser().getName());
        FileDO fileDO = fileService.get(getUser().getPicId());
        if (fileDO != null && fileDO.getUrl() != null)
        {
            if (fileService.isExist(fileDO.getUrl()))
            {
                model.addAttribute("picUrl", fileDO.getUrl());
            }
            else
            {
                model.addAttribute("picUrl", "/img/photo_s.jpg");
            }
        }
        else
        {
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        }
        model.addAttribute("username", getUser().getUsername());
        model.addAttribute("role", this.getUser().getName());
        return "index_v1";
    }

    @GetMapping("/login")
    String login()
    {
        return "login";
    }

    @Log("登录")
    @PostMapping("/login")
    @ResponseBody
    R ajaxLogin(String username, String password)
    {
        password = MD5Utils.encrypt(username, password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            return R.ok();
        }
        catch (AuthenticationException e)
        {
            return R.error("用户或密码错误");
        }
    }

    @GetMapping("/logout")
    String logout()
    {
        ShiroUtils.logout();
        return "redirect:/login";
    }

    @GetMapping("/main")
    String main()
    {
        return "main";
    }

    @GetMapping("/403")
    String error403()
    {
        return "403";
    }
}
