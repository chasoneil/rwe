package com.chason.rwe.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chason.rwe.domain.WhiteListDO;
import com.chason.rwe.service.WhiteListService;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.GUIDUtils;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;

/**
 * 空间信息
 * @author chason
 */

@Controller
@RequestMapping("/rtm/whitelist")
public class WhiteListController extends BaseController
{
    @Autowired
    private WhiteListService whiteListService;

    @GetMapping()
    @RequiresPermissions("rwe:whitelist:whitelist")
    String whitelist()
    {
        return "rwe/whitelist/whitelist";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rwe:whitelist:whitelist")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        // 查询列表数据
        Query query = new Query(params);
        List<WhiteListDO> whites = whiteListService.list(query);
        int total = whiteListService.count(query);
        PageUtils pageUtils = new PageUtils(whites, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("rwe:whitelist:whitelist")
    String add(Model model)
    {
        return "rwe/whitelist/add";
    }


    @GetMapping("/edit/{whId}")
    @RequiresPermissions("rwe:whitelist:whitelist")
    String edit(@PathVariable("whId") String whId, Model model)
    {
        WhiteListDO whiteList = whiteListService.get(whId);
        model.addAttribute("whiteList", whiteList);
        return "rwe/whitelist/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rwe:whitelist:whitelist")
    public R save(WhiteListDO whiteList)
    {
        if(whiteList.getWhFinishDate().before(whiteList.getWhStartDate()))
        {
            return R.error("结束时间必须在开始时间之后");
        }

        whiteList.setWhId(GUIDUtils.generateGUID(whiteList));
        if(whiteListService.save(whiteList) > 0)
        {
            return R.ok();
        }

        return R.error("操作失败");
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("rwe:whitelist:whitelist")
    public R update(WhiteListDO whiteList)
    {
        if(whiteList.getWhFinishDate().before(whiteList.getWhStartDate()))
        {
            return R.error("结束时间必须在开始时间之后");
        }

        if(whiteListService.update(whiteList) > 0)
        {
            return R.ok();
        }
        return R.error("操作失败");
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rwe:whitelist:whitelist")
    public R remove(String whId)
    {
        if(whiteListService.remove(whId) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("rwe:policy:remove")
    public R remove(@RequestParam("ids[]") String[] policyIds)
    {
        return R.error();
    }

}
