package com.chason.system.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.domain.Tree;
import com.chason.common.service.DictService;
import com.chason.common.utils.*;
import com.chason.system.domain.DeptDO;
import com.chason.system.domain.RoleDO;
import com.chason.system.domain.UserDO;
import com.chason.system.service.RoleService;
import com.chason.system.service.UserService;
import com.chason.system.vo.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController
{
    private String prefix = "system/user";
    @Autowired
    UserService    userService;
    @Autowired
    RoleService    roleService;
    @Autowired
    DictService    dictService;

    @RequiresPermissions("sys:user:user")
    @GetMapping("")
    String user(Model model)
    {
        return prefix + "/user";
    }

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params)
    {
        // 查询列表数据
        Query query = new Query(params);
        List<UserDO> sysUserList = userService.list(query);
        int total = userService.count(query);
        PageUtils pageUtil = new PageUtils(sysUserList, total);
        return pageUtil;
    }

    @RequiresPermissions("sys:user:add")
    @Log("添加用户")
    @GetMapping("/add")
    String add(Model model)
    {
        List<RoleDO> roles = roleService.list();
        model.addAttribute("roles", roles);
        return prefix + "/add";
    }

    @RequiresPermissions("sys:user:edit")
    @Log("编辑用户")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id)
    {
        UserDO userDO = userService.get(id);
        model.addAttribute("user", userDO);
        List<RoleDO> roles = roleService.list(id);
        model.addAttribute("roles", roles);
        return prefix + "/edit";
    }

    @RequiresPermissions("sys:user:add")
    @Log("保存用户")
    @PostMapping("/save")
    @ResponseBody
    R save(UserDO user)
    {
        user.setPassword(
                MD5Utils.encrypt(user.getUsername(), user.getPassword()));
        if (userService.save(user) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/update")
    @ResponseBody
    R update(UserDO user)
    {
        if (userService.update(user) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:edit")
    @Log("更新用户")
    @PostMapping("/updatePeronal")
    @ResponseBody
    R updatePeronal(UserDO user)
    {
        if (userService.updatePersonal(user) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:remove")
    @Log("删除用户")
    @PostMapping("/remove")
    @ResponseBody
    R remove(Long id)
    {
        if (userService.remove(id) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    @RequiresPermissions("sys:user:batchRemove")
    @Log("批量删除用户")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") Long[] userIds)
    {
        int r = userService.batchremove(userIds);
        if (r > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params)
    {
        // 存在，不通过，false
        return !userService.exit(params);
    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("请求更改用户密码")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable("id") Long userId, Model model)
    {
        UserDO userDO = new UserDO();
        userDO.setUserId(userId);
        model.addAttribute("user", userDO);
        return prefix + "/reset_pwd";
    }

    @Log("提交更改用户密码")
    @PostMapping("/resetPwd")
    @ResponseBody
    R resetPwd(UserVO userVO)
    {
        try
        {
            userService.resetPwd(userVO, getUser());
            return R.ok();
        }
        catch (Exception e)
        {
            return R.error(1, e.getMessage());
        }

    }

    @RequiresPermissions("sys:user:resetPwd")
    @Log("admin提交更改用户密码")
    @PostMapping("/adminResetPwd")
    @ResponseBody
    R adminResetPwd(UserVO userVO)
    {
        try
        {
            userService.adminResetPwd(userVO);
            return R.ok();
        }
        catch (Exception e)
        {
            return R.error(1, e.getMessage());
        }

    }

    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDO> tree()
    {
        Tree<DeptDO> tree = new Tree<DeptDO>();
        tree = userService.getTree();
        return tree;
    }

    @GetMapping("/spaceManagerTree/{managerIds}")
    @ResponseBody
    public Tree<DeptDO> spaceManagerTree(@PathVariable("managerIds") String managerIds)
    {
        if(managerIds == "" || managerIds == null)
        {
            return this.tree();
        }
        else
        {
            String[] strIds = managerIds.split(",");
            List<String> ids = new ArrayList<>();
            for (String theStr : strIds)
            {
                UserDO user = userService.get(Long.parseLong(theStr));
                if(user != null)
                {
                    ids.add(theStr);
                }
            }
            Tree<DeptDO> tree = new Tree<DeptDO>();
            tree = userService.getTree();
            Tree<DeptDO> newTree = setCheckTree(tree, ids);
            return newTree;
        }
    }

    /*
     * 递归调用，设置tree中的选中项
     */
    private Tree<DeptDO> setCheckTree(Tree<DeptDO> tree,List<String> ids)
    {

        if(ids.contains(tree.getId()))
        {
            tree.getState().put("selected", true);
        }
        if(tree.isHasChildren())
        {
            List<Tree<DeptDO>> childrenTree = tree.getChildren();
            for (Tree<DeptDO> theChildren : childrenTree)
            {
                setCheckTree(theChildren,ids);
            }
        }
        return tree;
    }

    @GetMapping("/treeView")
    String treeView()
    {
        return prefix + "/userTree";
    }

    @GetMapping("/personal")
    String personal(Model model)
    {
        UserDO userDO = userService.get(getUserId());
        model.addAttribute("user", userDO);
        model.addAttribute("hobbyList", dictService.getHobbyList(userDO));
        model.addAttribute("sexList", dictService.getSexList());
        return prefix + "/personal";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    R uploadImg(@RequestParam("avatar_file") MultipartFile file,
            String avatar_data, HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            result = userService.updatePersonalImg(file, avatar_data,
                    getUserId());
        }
        catch (Exception e)
        {
            return R.error("更新图像失败！");
        }
        if (result != null && result.size() > 0)
        {
            return R.ok(result);
        }
        else
        {
            return R.error("更新图像失败！");
        }
    }

    /**
     * 前台翻译的包装类
     * @author 12831
     */
    class UserDetail
    {
        private String name;

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }
}
