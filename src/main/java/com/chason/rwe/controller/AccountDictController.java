package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.AccountDictDO;
import com.chason.rwe.service.AccountDictService;
import com.chason.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 数据字典
 * @author Chason
 */

@Controller
@RequestMapping("/rwe/account/dict")
public class AccountDictController extends BaseController {

    private static final String PREFIX = "rwe/accountDict";

    @Autowired
    private AccountDictService accountDictService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public String index() {
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {

        int roleLevel = roleService.getRoleLevel(getUserId());
        if (roleLevel == 20) { // 普通用户
            params.put("userId", getUserId());
        }

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        Query query = new Query(params);
        List<AccountDictDO> accountDictList = accountDictService.list(query);
        int total = accountDictService.count(query);
        return new PageUtils(accountDictList, total);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(AccountDictDO accountDictDO) {
        try {
            accountDictDO.setDictName(accountDictDO.getDictName().replaceAll(",", ""));
            accountDictDO.setUserId(getUserId());

            if (accountDictService.exist(accountDictDO)) {
                return R.error("数据字典值已存在");
            }

            int save = accountDictService.save(accountDictDO);
            if (save != 1) {
                return R.error("新增数据字典失败");
            }
        } catch (Exception e) {
            return R.error("新增数据字典失败：" + e.getMessage());
        }
        return R.ok();
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") int id, Model model) {

        AccountDictDO accountDictDO = accountDictService.get(id);

        if (accountDictDO == null) {
            throw new RuntimeException("数据字典不存在");
        }

        model.addAttribute("dict", accountDictDO);
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(AccountDictDO accountDictDO) {
        try {
            accountDictDO.setDictName(accountDictDO.getDictName().replaceAll(",", ""));
            if (accountDictService.exist(accountDictDO)) {
                return R.error("数据字典值已存在");
            }
            int result = accountDictService.update(accountDictDO);
            return result > 0 ? R.ok("修改数据字典成功") : R.error("修改数据字典失败");
        }
        catch (Exception e) {
            return R.error("修改数据字典失败：" + e.getMessage());
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(Integer id) {
        AccountDictDO accountDictDO = accountDictService.get(id);
        if (accountDictDO.getSys() == 1) {
            return R.error("系统字典不能删除");
        }
        return accountDictService.remove(id) > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") int[] ids) {
        for (int id : ids) {
            AccountDictDO accountDictDO = accountDictService.get(id);
            if (accountDictDO.getSys() == 1) {
                return R.error("系统字典不能删除");
            }
        }
        int row = accountDictService.batchRemove(ids);
        return row > 0 ? R.ok("批量删除成功，共删除" + row + "条数据") : R.error();
    }

}

