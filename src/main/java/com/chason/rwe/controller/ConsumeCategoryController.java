package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.dict.AccountDict;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.service.ConsumeCategoryService;

import com.chason.rwe.service.DeepTypeService;
import com.chason.system.service.RoleService;
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

    private static final String PREFIX = "rwe/consume_category/";

    @Autowired
    private ConsumeCategoryService consumeCategoryService;

    @Autowired
    private DeepTypeService deepTypeService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public String index() {
        return PREFIX + "index";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        int roleLevel = roleService.getRoleLevel(getUserId());
        if (roleLevel == 20) { // 普通用户
            params.put("userId", getUserId());
        }

        Query query = new Query(params);
        List<ConsumeCategoryDO> consumeCategoryDOList = consumeCategoryService.list(query);
        int total = consumeCategoryService.count(query);
        return new PageUtils(consumeCategoryDOList, total);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "add";
    }

    @GetMapping("/addType/{id}")
    String addType(@PathVariable("id") int id, Model model) {
        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(id);
        if (consumeCategoryDO == null) {
            throw new RuntimeException("消费类型不存在");
        }

        long userId = getUserId();
        HashSet<String> deepTypeNames = AccountDict.getInstance().getDeepTypeDict(deepTypeService,
                roleService.getRoleLevel(userId), userId).get(userId);
        model.addAttribute("consumeCategory", consumeCategoryDO);
        model.addAttribute("deepTypeNames", deepTypeNames);
        return PREFIX + "addType";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(ConsumeCategoryDO consumeCategoryDO) {
        try {
            checkAdd(consumeCategoryDO, 1);
            consumeCategoryDO.setUserId(getUserId());
            consumeCategoryDO.setLevel(1);
            int save = consumeCategoryService.save(consumeCategoryDO);
            refreshNameDict();
            if (save != 1) {
                return R.error("新增消费类型失败");
            }
        } catch (Exception e) {
            return R.error("新增消费类型失败：" + e.getMessage());
        }
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/saveType")
    public R saveType(ConsumeCategoryDO consumeCategoryDO) {
        try {
            checkAdd(consumeCategoryDO,2);
            consumeCategoryDO.setLevel(2);
            int save = consumeCategoryService.save(consumeCategoryDO);
            refreshTypeDict();
            if (save != 1) {
                return R.error("新增消费细类失败");
            }
        } catch (Exception e) {
            return R.error("新增消费细类失败：" + e.getMessage());
        }
        return R.ok();
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") int id, Model model) {
        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(id);

        long userId = getUserId();

        HashSet<String> names = AccountDict.getInstance().getCategoryNameDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);
        HashSet<String> deepTypeNames = AccountDict.getInstance().getDeepTypeDict(deepTypeService,
                roleService.getRoleLevel(userId), getUserId()).get(userId);
        if (consumeCategoryDO == null) {
            throw new RuntimeException("消费类型不存在");
        }

        model.addAttribute("consumeCategory", consumeCategoryDO);
        model.addAttribute("names", names);
        model.addAttribute("deepTypeNames", deepTypeNames);
        return PREFIX + "edit";
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(ConsumeCategoryDO consumeCategoryDO) {
        try {
            checkUpdate(consumeCategoryDO, consumeCategoryDO.getLevel());

            if (consumeCategoryDO.getLevel() == 1) {
                String oldName = consumeCategoryDO.getBillType();
                String newName = consumeCategoryDO.getCategoryName();
                if (!oldName.equals(newName)) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("oldName", oldName);
                    params.put("newName", newName);
                    consumeCategoryService.updateCategory(params);
                    refreshNameDict();
                }
            } else {
                consumeCategoryService.update(consumeCategoryDO);
                refreshTypeDict();
            }
        }
        catch (Exception e) {
            return R.error("修改消费类型失败：" + e.getMessage());
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


    private void checkAdd(ConsumeCategoryDO consumeCategoryDO, int level) {

        if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryName())) {
            throw new RuntimeException("一级菜单不能为空");
        }

        long userId = getUserId();
        if (level == 1) {

            HashSet<String> categoryNameDict = AccountDict.getInstance().getCategoryNameDict(consumeCategoryService,
                    roleService.getRoleLevel(userId), userId).get(userId);
            if (categoryNameDict.contains(consumeCategoryDO.getCategoryName())) {
                throw new RuntimeException("一级菜单名称已存在");
            }
            consumeCategoryDO.setCategoryType("-");
            consumeCategoryDO.setBillType("-");
            consumeCategoryDO.setDeepType("-");
        }

        if (level == 2) {
            if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("二级菜单不能为空");
            }

            HashSet<String> categoryTypeDict = AccountDict.getInstance().getCategoryTypeDict(consumeCategoryService,
                    roleService.getRoleLevel(userId), userId).get(userId);
            if (categoryTypeDict.contains(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("二级菜单名称已存在");
            }

            if (!StringUtils.isNotNull(consumeCategoryDO.getBillType())) {
                consumeCategoryDO.setBillType("-");
            }

            if (!StringUtils.isNotNull(consumeCategoryDO.getDeepType())) {
                consumeCategoryDO.setDeepType("-");
            }
        }
    }

    private void checkUpdate(ConsumeCategoryDO consumeCategoryDO, int level) {

        if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryName())) {
            throw new RuntimeException("一级菜单不能为空");
        }

        if (level == 2) {
            if (!StringUtils.isNotNull(consumeCategoryDO.getCategoryType())) {
                throw new RuntimeException("二级菜单不能为空");
            }

            if (!StringUtils.isNotNull(consumeCategoryDO.getBillType())) {
                consumeCategoryDO.setBillType("-");
            }

            if (!StringUtils.isNotNull(consumeCategoryDO.getDeepType())) {
                consumeCategoryDO.setDeepType("-");
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


