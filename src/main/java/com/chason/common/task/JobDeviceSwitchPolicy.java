package com.chason.common.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.chason.common.annotation.Log;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.PolicyService;
import com.chason.rwe.service.SpaceService;
import com.chason.rwe.utils.PolicyTimeUtils;

/**
 * 设备每日运行策略刷新
 * */
@Component
public class JobDeviceSwitchPolicy implements Job
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private SpaceService spaceService;

    @Log("设备每日运行策略刷新")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        System.out.println("### start job:JobDeviceSwitchPolicy");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("devStatus", "11");
        List<DeviceDO> devices = deviceService.list(params);
        List<PolicyDO> policys = policyService.list(new HashMap<String, Object>());

        //List<SpaceDO> spaces = spaceService.list(new HashMap<String, Object>());

        for (DeviceDO theDevice : devices)
        {
            String strPolicy = "-";
            if ("自动".equals(theDevice.getDevSwitchMode()) && theDevice.getDevGroupCode() != null)
            {
                SpaceDO theSpace = findTerminalNodes(theDevice);//根据设备找到对应的空间
                if (theSpace != null)
                {
                    //根据空间找到对应的策略
                    List<String> policies = PolicyTimeUtils.findPolicy(policys, theSpace, theDevice, deviceService);
                    if (policies.size() > 0)
                    {
                        strPolicy = JSON.toJSONString(policies);
                    }//end if
                }//end if
            }//end if
            theDevice.setDevPolicy(strPolicy);
            deviceService.update(theDevice);
        }
    }

    /**
     * 找到设备对应的空间节点
     * 空间节点取最小单位(没有子节点)
     * */
    /*private SpaceDO findSpace(List<SpaceDO> spaces, DeviceDO theDevice)
    {
        for (SpaceDO theSpace : spaces)
        {
            if (theDevice.getDevGroupCode().indexOf(theSpace.getSpaceCode()) == -1)
            {
                continue;
            }
            return theSpace;
        }
        return null;
    }*/

    /**
     * 获取节点中的末梢节点
     * @return
     */
    private SpaceDO findTerminalNodes(DeviceDO theDevice)
    {
        //获取设备的分组号
        String codes = theDevice.getDevGroupCode();
        List<String> tmp = new ArrayList<>();
        //一台设备只能绑定一个组（位置） codes一定是父子关系
        if(StringUtils.isNotNull(codes))
        {
            //存放一组space的临时集合
            String[] spaceCodes = codes.split(",");
            for (String code : spaceCodes)
            {
                SpaceDO theSpace = spaceService.findByCode(code);
                tmp.add(theSpace.getSpaceId());
            }
        }
        List<String> spaceId = spaceService.getTerminal(tmp);
        return spaceService.get(spaceId.get(0));
    }

}
