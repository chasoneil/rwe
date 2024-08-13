package com.chason.rwe.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.PolicyService;
import com.chason.rwe.service.SpaceService;
import com.chason.common.controller.BaseController;
import com.chason.common.domain.DictDO;
import com.chason.common.service.DictService;
import com.chason.common.utils.GUIDUtils;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;

/**
 * 空间信息
 * @author chason
 */

@Controller
@RequestMapping("/rtm/policy")
public class PolicyController extends BaseController
{
    @Autowired
    private PolicyService policyService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private DictService dictService;

    @GetMapping()
    @RequiresPermissions("rwe:policy:policy")
    String policy()
    {
        return "rwe/policy/policy";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rwe:policy:policy")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        String spaceId = (String) params.get("spaceId");
        if(!"root".equals(spaceId))
        {
            params.put("policySpaceKeys", spaceId);
        }

        // 查询列表数据
        Query query = new Query(params);
        List<PolicyDO> policys = policyService.list(query);

        for (PolicyDO thePolicy : policys) //星期和空间翻译
        {
            String weekStr = "";
            String week = thePolicy.getPolicyWeek();

            String[] weekNum = week.split(",");
            for (String str : weekNum)
            {
                if("1".equals(str))
                {
                    weekStr += "星期日 ";
                }
                if("2".equals(str))
                {
                    weekStr += "星期一 ";
                }
                if("3".equals(str))
                {
                    weekStr += "星期二 ";
                }
                if("4".equals(str))
                {
                    weekStr += "星期三 ";
                }
                if("5".equals(str))
                {
                    weekStr += "星期四 ";
                }
                if("6".equals(str))
                {
                    weekStr += "星期五 ";
                }
                if("7".equals(str))
                {
                    weekStr += "星期六 ";
                }
            }
            thePolicy.setPolicyWeek(weekStr);
        }
        int total = policyService.count(query);
        PageUtils pageUtils = new PageUtils(policys, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("rwe:policy:add")
    String add(Model model)
    {
        Map<String, Object> param = new HashMap<>();
        List<DeviceDO> devices = deviceService.listValid(param);
        List<DictDO> types = dictService.listByType("policy_work_type");

        model.addAttribute("devices", devices);
        model.addAttribute("types", types);
        return "rwe/policy/add";
    }

    @GetMapping("/addRandom")
    @RequiresPermissions("rwe:policy:add")
    String addRandom(Model model)
    {
        return "rwe/policy/addRandom";
    }

    @GetMapping("/edit/{policyId}")
    @RequiresPermissions("rwe:policy:add")
    String edit(@PathVariable("policyId") String policyId, Model model)
    {
        PolicyDO thePolicy = policyService.get(policyId);
        String policySpaceKeys = thePolicy.getPolicySpaceKeys();
        String[] pKeys = policySpaceKeys.split(",");

        List<String> keys = new ArrayList<>();
        List<SpaceDO> spaces = spaceService.list(new HashMap<>());
        for (SpaceDO space : spaces)
        {
            if(StringUtils.isNotNull(space.getSpaceParentId()))
            {
                keys.add(space.getSpaceParentId());
            }
        }

        String key = "";
        for (String strKey : pKeys)
        {
            if("-1".equals(strKey))
            {
                continue;
            }
            if(isLastNode(keys, strKey))
            {
                key += (strKey + ",");
            }
        }

        List<DictDO> types = dictService.listByType("policy_work_type");
        model.addAttribute("policy", thePolicy);
        model.addAttribute("types", types);
        model.addAttribute("keys", key);
        if("随机策略".equals(thePolicy.getPolicyType()))
        {
            return "rwe/policy/randomEdit";
        }

        return "rwe/policy/edit";
    }

    @GetMapping("/detail/{policyId}")
    String detail(@PathVariable("policyId") String policyId, Model model)
    {
        PolicyDO thePolicy = policyService.get(policyId);
        if(thePolicy != null)
        {
            String spaceName = "";
            String[] strIds = thePolicy.getPolicySpaceKeys().split(",");

            String weekStr = "";
            String week = thePolicy.getPolicyWeek();
            String[] weekNum = week.split(",");
            for (String str : weekNum)
            {
                if("1".equals(str))
                {
                    weekStr += "星期日 ";
                }
                if("2".equals(str))
                {
                    weekStr += "星期一 ";
                }
                if("3".equals(str))
                {
                    weekStr += "星期二 ";
                }
                if("4".equals(str))
                {
                    weekStr += "星期三 ";
                }
                if("5".equals(str))
                {
                    weekStr += "星期四 ";
                }
                if("6".equals(str))
                {
                    weekStr += "星期五 ";
                }
                if("7".equals(str))
                {
                    weekStr += "星期六 ";
                }
            }
            thePolicy.setPolicyWeek(weekStr);
            for (String strId : strIds)
            {
                SpaceDO theSpace = spaceService.get(strId);
                if(theSpace != null)
                {
                    spaceName += (theSpace.getSpaceAddress() + ",");
                }
            }
            thePolicy.setPolicySpaceKeys(spaceName);
        }
        model.addAttribute("policy", thePolicy);
        return "rwe/policy/detail";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rwe:policy:add")
    public R save(PolicyDO policy)
    {
        try
        {
            String dur = policy.getPolicyDur();
            int durTime = Integer.parseInt(dur);
            if(durTime > 180)
            {
                return R.error("连续开机最多只能维持3小时。");
            }
        }
        catch (Exception e)
        {
            return R.error("持续时间输入错误,只能输入180以内的正整数");
        }

        if("other".equals(policy.getPolicyType()) && policy.getPolicyWeek() == null)
        {
            return R.error("策略类型为自定义工作日时,则适用星期必填");
        }

        if(policy.getPolicyWeek() == null)
        {
            if("odd".equals(policy.getPolicyType())) //工作日单
            {
                policy.setPolicyWeek("2,4,6");
            }
            if("even".equals(policy.getPolicyType())) //工作日双
            {
                policy.setPolicyWeek("3,5");
            }
        }

        policy.setPolicyId(GUIDUtils.generateGUID(policy));
        if(!StringUtils.isNotNull(policy.getPolicyName()))
        {
            policy.setPolicyName("普通");
        }
        policyService.save(policy);
        return R.ok();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("rwe:space:update")
    public R update(PolicyDO policy)
    {
        try
        {
            String dur = policy.getPolicyDur();
            int durTime = Integer.parseInt(dur);
            if(durTime > 180)
            {
                return R.error("连续开机最多只能维持3小时。");
            }
        }
        catch (Exception e)
        {
            return R.error("持续时间输入错误,只能输入180以内的正整数");
        }

        if("other".equals(policy.getPolicyType()) && policy.getPolicyWeek() == null)
        {
            return R.error("策略类型为自定义工作日时,则适用星期必填");
        }

        if(policy.getPolicyWeek() == null)
        {
            if("odd".equals(policy.getPolicyType())) //工作日单
            {
                policy.setPolicyWeek("2,4,6");
            }
            if("even".equals(policy.getPolicyType())) //工作日双
            {
                policy.setPolicyWeek("3,5");
            }
        }

        if(!StringUtils.isNotNull(policy.getPolicyName()))
        {
            policy.setPolicyName("普通");
        }
        policyService.update(policy);
        //spaceService.update(space);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rwe:policy:remove")
    public R remove(String policyId)
    {
        PolicyDO thePolicy = policyService.get(policyId);
        if("随机策略".equals(thePolicy.getPolicyType()))
        {
            Map<String, Object> param = new HashMap<>();
            List<DeviceDO> devices = deviceService.list(param);
            for (DeviceDO device : devices)
            {
                if(StringUtils.isNotNull(device.getDevRandom()))
                {
                    String str = device.getDevRandom();
                    if(str.indexOf(thePolicy.getPolicyId()) != -1)
                    {
                        str = str.replace(thePolicy.getPolicyId(), "");
                        device.setDevRandom(str);
                        deviceService.update(device);
                    }
                }
            }
        }

        int count = policyService.remove(policyId);
        if(count > 0)
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
        int count = policyService.batchRemove(policyIds);
        if(count > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 判断一个节点是不是末梢节点 (即没有子节点)
     * @param spaceId 空间的id
     * @return
     */
    private boolean isLastNode(List<String> parents,String spaceId)
    {
        if(parents.contains(spaceId))
        {
            return false;
        }
        return true;
    }
}
