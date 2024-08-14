package com.chason.rwe.controller;

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
import com.chason.system.domain.UserDO;
import com.chason.system.service.UserService;
import com.chason.common.controller.BaseController;
import com.chason.common.domain.Tree;
import com.chason.common.utils.GUIDUtils;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;

/**
 * 空间信息
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-11-22 15:43:51
 */

@Controller
@RequestMapping("/rtm/space")
public class SpaceController extends BaseController
{
    @Autowired
    private SpaceService spaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PolicyService policyService;

    @GetMapping()
    @RequiresPermissions("rtm:space:space")
    String Space()
    {
        return "rwe/space/space";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rtm:space:space")
    public List<SpaceDO> list()
    {
        Map<String, Object> query = new HashMap<>(16);
        query.put("order", "asc");
        // 查询列表数据
        List<SpaceDO> spaceList = spaceService.list(query);
        /*
         * 翻译页面上的管理员姓名
         */
        for (SpaceDO theSpace : spaceList)
        {
            String[] ids = theSpace.getSpaceManagerBy().split(",");
            if(ids.length > 0)
            {
                String strManager = "";
                for (String strId : ids)
                {
                    try
                    {
                        UserDO theUser = userService.get(Long.parseLong(strId));
                        if(theUser != null)
                        {
                            strManager += (theUser.getUsername() + " ");
                        }
                    }
                    catch (Exception e)
                    {
                        System.err.println("****管理员的id不能转化成long类型的数据****");
                        continue;
                    }
                }
                if(strManager != "" || strManager != null)
                {
                    theSpace.setSpaceManagerBy(strManager);
                }
            }
        }
        return spaceList;
    }

    @GetMapping("/add/{id}")
    @RequiresPermissions("rtm:space:add")
    String add(@PathVariable("id") String id, Model model)
    {
        if ("root".equals(id))
        {
            model.addAttribute("pAddress", "全部");
        }
        else
        {
            model.addAttribute("id", id);
            model.addAttribute("pAddress", spaceService.get(id).getSpaceAddress());
        }
        return "rwe/space/add";
    }

    @GetMapping("/edit/{spaceId}")
    @RequiresPermissions("rwe:space:update")
    String edit(@PathVariable("spaceId") String spaceId, Model model)
    {
        SpaceDO space = spaceService.get(spaceId);
        model.addAttribute("space", space);
        return "rwe/space/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rtm:space:update")
    public R save(SpaceDO space)
    {
        String key = GUIDUtils.generateGUID(space);
        space.setSpaceId(key); // 设置主键
        if("root".equals(space.getSpaceParentId()))
        {
            space.setSpaceParentId("");
        }
        space.setSpaceCode(key); //唯一标识符code用主键代替
        int count = spaceService.save(space);
        if (count > 0)
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
    @RequiresPermissions("rtm:space:update")
    public R update(SpaceDO space)
    {
        SpaceDO theSpace = spaceService.get(space.getSpaceId());
        if(theSpace == null)
        {
            return R.error("分组不存在或已删除");
        }
        spaceService.update(space);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("rtm:space:delete")
    public R remove(String spaceId)
    {
        SpaceDO theSpace = spaceService.get(spaceId);
        String code = theSpace.getSpaceCode();
        List<DeviceDO> devices = deviceService.findAllValidByGroupCode(code);
        if(devices.size() > 0)
        {
            return R.error("该空间下还存在设备,请先解除设备空间绑定关系");
        }

        Map<String, Object> map = new HashMap<>();
        List<PolicyDO> policys = policyService.list(map);
        for (PolicyDO thePolicy : policys)
        {
            if(StringUtils.isNotNull(thePolicy.getPolicySpaceKeys()))
            {
                if(thePolicy.getPolicySpaceKeys().indexOf(spaceId) != -1)
                {
                    thePolicy.setPolicySpaceKeys(thePolicy.getPolicySpaceKeys().replace(spaceId, ""));
                    policyService.update(thePolicy);
                }
            }
        }

        spaceService.remove(spaceId);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("rtm:space:delete")
    public R remove(@RequestParam("ids[]") String[] spaceIds)
    {
        spaceService.batchRemove(spaceIds);
        return R.ok();
    }

    /**
     * 树形结构
     * @param type
     */
    @GetMapping("/tree")
    @ResponseBody
    public Tree<SpaceDO> tree()
    {
        Tree<SpaceDO> tree = new Tree<SpaceDO>();
        UserDO user = new UserDO();
        tree = spaceService.getTree(user);
        return tree;
    }

    /**
     * 获取指定类型的树形结构
     * @param type
     */
    @GetMapping("/treeByManager")
    @ResponseBody
    public Tree<SpaceDO> treeByManager()
    {
        Tree<SpaceDO> tree = new Tree<SpaceDO>();
        tree = spaceService.getTreeByUser(super.getUser());
        return tree;
    }

}
