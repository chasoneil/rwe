package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.KeepAccountDO;
import com.chason.rwe.service.KeepAccountService;
import com.chason.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 记账信息
 * keep account controller
 * @author Chason
 * @date 2024/12/30
 */

@Controller
@RequestMapping("/rwe/keep_account")
public class KeepAccountController extends BaseController {

    private static final String PREFIX = "rwe/keep_account";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private KeepAccountService keepAccountService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public String index() {
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {

        // 获取当前的用户信息
        int roleLevel = roleService.getRoleLevel(getUserId());
        if (roleLevel == 20) { // 普通用户
            params.put("createUserId", getUserId());
        }

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        Query query = new Query(params);
        List<KeepAccountDO> keepAccountDOList = keepAccountService.list(query);
        int total = keepAccountService.count(query);
        return new PageUtils(keepAccountDOList, total);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "/add";
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(Integer id) {
        return keepAccountService.remove(id) > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") int[] ids) {
        int row = keepAccountService.batchRemove(ids);
        return row > 0 ? R.ok("批量删除成功，共删除" + row + "条数据") : R.error();
    }
}


