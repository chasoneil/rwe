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

/**
 * 字典表
 */
@Controller
@RequestMapping("/common/sysDict")
public class DictController extends BaseController
{
    @Autowired
    private DictService sysDictService;

    @GetMapping()
    @RequiresPermissions("common:sysDict:sysDict")
    String sysDict()
    {
        return "common/sysDict/sysDict";
    }

    /**
     * 查询列表数据
     * */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysDict:sysDict")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        Query query = new Query(params);
        List<DictDO> sysDictList = sysDictService.list(query);
        int total = sysDictService.count(query);
        PageUtils pageUtils = new PageUtils(sysDictList, total);
        return pageUtils;
    }

    /**
     * 添加页
     * */
    @GetMapping("/add")
    @RequiresPermissions("common:sysDict:add")
    String add()
    {
        return "common/sysDict/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:sysDict:edit")
    String edit(@PathVariable("id") Long id, Model model)
    {
        DictDO sysDict = sysDictService.get(id);
        model.addAttribute("sysDict", sysDict);
        return "common/sysDict/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:sysDict:add")
    public R save(DictDO sysDict)
    {
        if (sysDictService.save(sysDict) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("common:sysDict:edit")
    public R update(DictDO sysDict)
    {
        sysDictService.update(sysDict);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:sysDict:remove")
    public R remove(Long id)
    {
        if (sysDictService.remove(id) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 批量删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:sysDict:batchRemove")
    public R batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        sysDictService.batchRemove(ids);
        return R.ok();
    }

    /**
     * 列出所有分类
     * @return json或xml
     * */
    @GetMapping("/type")
    @ResponseBody
    public List<DictDO> listType()
    {
        return sysDictService.listType();
    };

    // 类别已经指定增加
    @GetMapping("/add/{type}/{description}")
    @RequiresPermissions("common:sysDict:add")
    String addD(
            Model model,
            @PathVariable("type") String type,
            @PathVariable("description") String description)
    {
        model.addAttribute("type", type);
        model.addAttribute("description", description);
        return "common/sysDict/add";
    }

    @ResponseBody
    @GetMapping("/list/{type}")
    public List<DictDO> listByType(@PathVariable("type") String type)
    {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        List<DictDO> dictList = sysDictService.list(map);
        return dictList;
    }

    /**
     * 获取指定类型的树形结构
     * @param type
     */
    @GetMapping("/tree")
    @ResponseBody
    public Tree<DictDO> tree(@RequestParam("type") String type)
    {
    	Tree<DictDO> tree = new Tree<DictDO>();
        tree = sysDictService.getTree(type);
        return tree;
    }

    /**
     * 获取设备分组树形结构
     * @param type
     */
    @GetMapping("/deviceGroupTree")
    @ResponseBody
    public Tree<DictDO> deviceGroupTree(@RequestParam("type") String type)
    {
        Tree<DictDO> tree = new Tree<DictDO>();
        tree = sysDictService.getDeviceGroupTree(type);
        return tree;
    }

    /**
     * 获得数据字典中指定的name，用于页面中显示
     * @param type
     * @param value
     * */
    @GetMapping("/detail")
    @ResponseBody
    public DictDO detail(@RequestParam("type") String type, @RequestParam("value") String value)
    {
        return sysDictService.getDictDoByTypeAndValue(type, value);
    };
}
