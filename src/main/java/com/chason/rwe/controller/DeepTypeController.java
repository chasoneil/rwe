package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.dict.AccountDict;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DeepTypeDO;
import com.chason.rwe.service.DeepTypeService;
import com.chason.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 深度支出分类  deep_type
 * @author Chason
 * @date 2025/1/2
 */

@Controller
@RequestMapping("/rwe/deepType")
public class DeepTypeController extends BaseController {

    private static final String PREFIX = "rwe/deep_type/";

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
        List<DeepTypeDO> deepTypeDOList = deepTypeService.list(query);
        int total = deepTypeService.count(query);
        return new PageUtils(deepTypeDOList, total);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "add";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(DeepTypeDO deepTypeDO) {
        try {
            check(deepTypeDO);
            deepTypeDO.setUserId(getUserId());
            int save = deepTypeService.save(deepTypeDO);
            refreshDict();
            if (save != 1) {
                return R.error("新增深度支出类型失败");
            }
        } catch (Exception e) {
            return R.error("新增深度支出类型失败：" + e.getMessage());
        }
        return R.ok();
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") int id, Model model) {
        DeepTypeDO deepTypeDO = deepTypeService.get(id);
        if (deepTypeDO == null) {
            throw new RuntimeException("深度支出类型不存在");
        }
        model.addAttribute("deepType", deepTypeDO);
        return PREFIX + "edit";
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(DeepTypeDO deepTypeDO) {
        try {
            check(deepTypeDO);
            int result = deepTypeService.update(deepTypeDO);
            refreshDict();
            return result > 0 ? R.ok("修改成功") : R.error("修改失败");
        }
        catch (Exception e) {
            return R.error("修改失败：" + e.getMessage());
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(Integer id) {
        int result = deepTypeService.remove(id);
        refreshDict();
        return  result > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") int[] ids) {
        int row = deepTypeService.batchRemove(ids);
        refreshDict();
        return row > 0 ? R.ok("批量删除成功，共删除" + row + "条数据") : R.error();
    }


    private void check(DeepTypeDO deepTypeDO) {

        if (!StringUtils.isNotNull(deepTypeDO.getDeepTypeName())) {
           throw new RuntimeException("深度支出名称不能为空");
        }

        long userId = getUserId();
        HashSet<String> names = AccountDict.getInstance().getDeepTypeDict(deepTypeService,
                roleService.getRoleLevel(userId), userId).get(userId);
        if (names.contains(deepTypeDO.getDeepTypeName())) {
            throw new RuntimeException("深度支出名称已存在");
        }
    }

    private void refreshDict() {
        long userId = getUserId();
        AccountDict.getInstance().initDeepTypeDict(deepTypeService, roleService.getRoleLevel(userId), userId);
    }
}


