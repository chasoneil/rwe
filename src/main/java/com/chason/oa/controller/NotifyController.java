package com.chason.oa.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chason.common.config.Constant;
import com.chason.common.controller.BaseController;
import com.chason.common.domain.DictDO;
import com.chason.common.service.DictService;
import com.chason.common.utils.MsgNotifyService;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.oa.domain.NotifyDO;
import com.chason.oa.domain.NotifyRecordDO;
import com.chason.oa.service.NotifyRecordService;
import com.chason.oa.service.NotifyService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知通告
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-05 17:11:16
 */

@Controller
@RequestMapping("/oa/notify")
public class NotifyController extends BaseController
{
    @Autowired
    private NotifyService       notifyService;

    @Autowired
    private NotifyRecordService notifyRecordService;

    @Autowired
    private DictService         dictService;

    @Autowired
    private  MsgNotifyService _msgNotifyService;

    @GetMapping()
    @RequiresPermissions("oa:notify:notify")
    String oaNotify()
    {
    	System.out.println("notify()---->");
        return "oa/notify/notify";
    }

    /**
     * 列出所有通知公告
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("oa:notify:notify")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        // 查询列表数据
        Query query = new Query(params);
        List<NotifyDO> notifyList = notifyService.list(query);
        int total = notifyService.count(query);
        PageUtils pageUtils = new PageUtils(notifyList, total);
        return pageUtils;
    }

    /**
     * 添加通知公告
     */
    @GetMapping("/add")
    @RequiresPermissions("oa:notify:add")
    String add()
    {
        return "oa/notify/add";
    }

    /**
     * 修改通知公告
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("oa:notify:edit")
    String edit(@PathVariable("id") Long id, Model model)
    {
        NotifyDO notify = notifyService.get(id);
        model.addAttribute("notify", notify);

        System.out.println("notufy edit()");
        // 获取所有的通知公告类型
        String type = notify.getType();
        List<DictDO> dictDOS = dictService.listByType("oa_notify_type");
        for (DictDO dictDO : dictDOS)
        {
            if (type.equals(dictDO.getName()))
            {
                dictDO.setRemarks("checked"); // 利用remarks字段保存选中的信息，供页面调用
            }
        }
        model.addAttribute("oaNotifyTypes", dictDOS);
        return "oa/notify/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("oa:notify:add")
    public R save(NotifyDO notify)
    {
        notify.setUpdateBy(super.getUsername());
        notify.setCreateBy(super.getUserId());
        if (notifyService.save(notify) > 0)
        {
            this._msgNotifyService.sendMsgToReceiver(notify.getUserIds(), "新消息：" + notify.getTitle());
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("oa:notify:edit")
    public R update(NotifyDO notify)
    {
        notify.setUpdateBy(super.getUsername());
        notifyService.update(notify);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("oa:notify:remove")
    public R remove(@RequestParam("id") Long id)
    {
        if (notifyService.remove(id) > 0)
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
    @RequiresPermissions("oa:notify:batchRemove")
    public R remove(@RequestParam("ids[]") Long[] ids)
    {
        notifyService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @GetMapping("/message")
    PageUtils message()
    {
        Map<String, Object> params = new HashMap<>(16);
        params.put("offset", 0);
        params.put("limit", 3);
        Query query = new Query(params);
        query.put("userId", getUserId());
        query.put("isRead", Constant.OA_NOTIFY_READ_NO);
        return notifyService.selfList(query);
    }

    @GetMapping("/selfNotify")
    String selefNotify()
    {
        return "oa/notify/selfNotify";
    }

    /**
     * 我的通知
     */
    @ResponseBody
    @GetMapping("/selfList")
    PageUtils selfList(@RequestParam Map<String, Object> params)
    {
        Query query = new Query(params);
        query.put("userId", getUserId());

        return notifyService.selfList(query);
    }

    /**
     * 点击查看
     */
    @GetMapping("/read/{id}")
    @RequiresPermissions("oa:notify:notify")
    String read(@PathVariable("id") Long id, Model model)
    {
        NotifyDO notify = notifyService.get(id);
        // 更改阅读状态
        NotifyRecordDO notifyRecordDO = new NotifyRecordDO();
        notifyRecordDO.setNotifyId(id);
        notifyRecordDO.setUserId(getUserId());
        notifyRecordDO.setReadDate(new Date());
        notifyRecordDO.setIsRead(Constant.OA_NOTIFY_READ_YES);
        notifyRecordService.changeRead(notifyRecordDO);

        model.addAttribute("notify", notify);

        return "oa/notify/read";
    }
}
