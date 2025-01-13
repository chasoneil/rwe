package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.dict.AccountDict;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.AccountDictDO;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.service.AccountDictService;
import com.chason.rwe.service.ConsumeCategoryService;

import com.chason.system.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * consume_category 记账分类
 * @author Chason
 * @date 2025/1/1
 */

@Controller
@RequestMapping("/rwe/consume_category")
public class ConsumeCategoryController extends BaseController {

    private static final String PREFIX = "rwe/consume_category";

    @Autowired
    private ConsumeCategoryService consumeCategoryService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountDictService accountDictService;

    @GetMapping()
    public String index() {
        return PREFIX + "/index";
    }

    @ApiOperation("获取账单字典列表")
    @ResponseBody
    @GetMapping("/list")
    public List<ConsumeCategoryDO> list(@RequestParam Map<String, Object> params) {

        int roleLevel = roleService.getRoleLevel(getUserId());
        if (roleLevel == 20) { // 普通用户
            params.put("userId", getUserId());
        }
        return consumeCategoryService.list(params);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "/add";
    }

    @GetMapping("/addType/{id}")
    String addType(@PathVariable("id") int id, Model model) {
        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(id);
        if (consumeCategoryDO == null) {
            throw new RuntimeException("一级菜单不存在");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("dictName", "深度支出分类");
        List<AccountDictDO> deeps = accountDictService.list(params);
        params.put("dictName", "账单周期");
        List<AccountDictDO> periods = accountDictService.list(params);

        model.addAttribute("consumeCategory", consumeCategoryDO);
        model.addAttribute("deeps", deeps);
        model.addAttribute("periods", periods);
        return PREFIX + "/addType";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(ConsumeCategoryDO consumeCategoryDO) {
        try {
            check(consumeCategoryDO, 1);
            consumeCategoryDO.setUserId(getUserId());
            consumeCategoryDO.setLevel(1);
            consumeCategoryDO.setParentId(0);

            // 新增一级菜单时，同时新增二级菜单
            consumeCategoryDO.setCategoryType(consumeCategoryDO.getCategoryName());
            consumeCategoryDO.setBillPeriod("-");
            consumeCategoryDO.setDeepType("-");
            int save = consumeCategoryService.save(consumeCategoryDO);
            refreshNameDict();
            if (save != 1) {
                return R.error("新增一级菜单失败");
            }
        } catch (Exception e) {
            return R.error("新增一级菜单失败：" + e.getMessage());
        }
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/saveType")
    public R saveType(ConsumeCategoryDO consumeCategoryDO) {
        try {
            check(consumeCategoryDO,2);

            double remainBudget = consumeCategoryService.remainBudget(consumeCategoryDO);
            if (remainBudget < consumeCategoryDO.getBudget()) {
                throw new RuntimeException("预算超出总预算");
            }

            consumeCategoryDO.setLevel(2);
            if (!StringUtils.isNotNull(consumeCategoryDO.getBillPeriod())) {
                consumeCategoryDO.setBillPeriod("-");
            }

            if (!StringUtils.isNotNull(consumeCategoryDO.getDeepType())) {
                consumeCategoryDO.setDeepType("-");
            }
            int save = consumeCategoryService.save(consumeCategoryDO);
            refreshTypeDict();
            if (save != 1) {
                return R.error("新增二级菜单失败");
            }
        } catch (Exception e) {
            return R.error("新增二级菜单失败：" + e.getMessage());
        }
        return R.ok();
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") int id, Model model) {

        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(id);

        long userId = getUserId();
        HashSet<String> names = AccountDict.getInstance().getCategoryNameDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);

        Map<String, Object> params = new HashMap<>();
        params.put("dictName", "深度支出分类");
        List<AccountDictDO> deeps = accountDictService.list(params);
        params.put("dictName", "账单周期");
        List<AccountDictDO> periods = accountDictService.list(params);

        if (consumeCategoryDO == null) {
            throw new RuntimeException("类型不存在");
        }

        model.addAttribute("consumeCategory", consumeCategoryDO);
        model.addAttribute("names", names);
        model.addAttribute("deeps", deeps);
        model.addAttribute("periods", periods);
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(ConsumeCategoryDO consumeCategoryDO) {
        try {
            check(consumeCategoryDO, consumeCategoryDO.getLevel());
            double remainBudget = consumeCategoryService.remainBudget(consumeCategoryDO);
            if (consumeCategoryDO.getLevel() == 1) {
                String oldName = consumeCategoryDO.getBillPeriod();
                String newName = consumeCategoryDO.getCategoryName();
                consumeCategoryDO.setBillPeriod("-");

                if (consumeCategoryDO.getBudget() - remainBudget < 0) {
                    throw new RuntimeException("预算超出总预算");
                }

                if (!oldName.equals(newName)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("oldName", oldName);
                    params.put("newName", newName);
                    consumeCategoryService.updateCategory(params);

                    refreshNameDict();
                }
            } else {
                if (remainBudget < consumeCategoryDO.getBudget()) {
                    throw new RuntimeException("预算超出总预算");
                }
                if (!StringUtils.isNotNull(consumeCategoryDO.getBillPeriod())) {
                    consumeCategoryDO.setBillPeriod("-");
                }
                if (!StringUtils.isNotNull(consumeCategoryDO.getDeepType())) {
                    consumeCategoryDO.setDeepType("-");
                }

                refreshTypeDict();
            }
            consumeCategoryService.update(consumeCategoryDO);
        }
        catch (Exception e) {
            return R.error("修改字典失败：" + e.getMessage());
        }
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(Integer id) {

        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(id);
        int result = 0;
        if (consumeCategoryDO.getLevel() == 1) {
            result = consumeCategoryService.removeCategory(consumeCategoryDO);
        } else {
            result = consumeCategoryService.remove(id);
        }
        refreshNameDict();
        refreshTypeDict();
        return  result> 0 ? R.ok("删除成功") : R.error("删除失败");
    }


    private void check(ConsumeCategoryDO consumeCategoryDO, int level) {

        if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryName())) {
            throw new RuntimeException("一级菜单不能为空");
        }

        if (!StringUtils.isNotNull(consumeCategoryDO.getInOut())) {
            throw new RuntimeException("收支类型不能为空");
        }

        if (consumeCategoryDO.getBudget() < 0) {
            throw new RuntimeException("预算必须大于0");
        }

        long userId = getUserId();
        HashSet<String> categoryNameDict = AccountDict.getInstance().getCategoryNameDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);
        HashSet<String> categoryTypeDict = AccountDict.getInstance().getCategoryTypeDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);

        if (level == 1) {
            if (categoryNameDict.contains(consumeCategoryDO.getCategoryName())) {
                throw new RuntimeException("一级菜单名称已存在");
            }
//            if (categoryTypeDict.contains(consumeCategoryDO.getCategoryName())) {
//                throw new RuntimeException("存在同名二级菜单");
//            }
        }

        if (level == 2) {
            if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("二级菜单不能为空");
            }

            if (categoryTypeDict.contains(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("二级菜单名称已存在");
            }

            if (categoryNameDict.contains(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("存在同名一级菜单");
            }
        }
    }

    private void refreshNameDict() {
        long userId = getUserId();
        AccountDict.getInstance().initCategoryNameDict(consumeCategoryService, roleService.getRoleLevel(userId), userId);
    }

    private void refreshTypeDict() {
        long userId = getUserId();
        AccountDict.getInstance().initCategoryTypeDict(consumeCategoryService, roleService.getRoleLevel(userId), userId);
    }
}


