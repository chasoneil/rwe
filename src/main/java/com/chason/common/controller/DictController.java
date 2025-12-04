package com.chason.common.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chason.common.domain.DictDO;
import com.chason.common.domain.Tree;
import com.chason.common.service.DictService;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common/sysDict")
public class DictController extends BaseController {

    @Autowired
    private DictService sysDictService;

    @GetMapping()
    @RequiresPermissions("common:sysDict:sysDict")
    String sysDict() {
        return "common/sysDict/sysDict";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysDict:sysDict")
    PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<DictDO> sysDictList = sysDictService.list(query);
        int total = sysDictService.count(query);
        return new PageUtils(sysDictList, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("common:sysDict:add")
    String add() {
        return "common/sysDict/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:sysDict:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        DictDO sysDict = sysDictService.get(id);
        model.addAttribute("sysDict", sysDict);
        return "common/sysDict/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:sysDict:add")
    R save(DictDO sysDict) {
        if (sysDictService.save(sysDict) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:sysDict:edit")
    R update(DictDO sysDict) {
        sysDictService.update(sysDict);
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:sysDict:remove")
    R remove(Long id) {
        if (sysDictService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @ResponseBody
    @PostMapping("/batchRemove")
    @RequiresPermissions("common:sysDict:batchRemove")
    R batchRemove(@RequestParam("ids[]") Long[] ids) {
        sysDictService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/type")
    List<DictDO> listType() {
        return sysDictService.listType();
    };

    // 类别已经指定增加
    @GetMapping("/add/{type}/{description}")
    @RequiresPermissions("common:sysDict:add")
    String addType(
            Model model,
            @PathVariable("type") String type,
            @PathVariable("description") String description) {
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "common/sysDict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    List<DictDO> listByType(@PathVariable("type") String type) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        return sysDictService.list(map);
    }

    @ResponseBody
    @GetMapping("/tree")
    Tree<DictDO> tree(@RequestParam("type") String type) {
        return sysDictService.getTree(type);
    }

    @GetMapping("/deviceGroupTree")
    @ResponseBody
    Tree<DictDO> deviceGroupTree(@RequestParam("type") String type) {
        return sysDictService.getDeviceGroupTree(type);
    }

    @GetMapping("/detail")
    @ResponseBody
    DictDO detail(@RequestParam("type") String type, @RequestParam("value") String value) {
        return sysDictService.getDictDoByTypeAndValue(type, value);
    }
}
