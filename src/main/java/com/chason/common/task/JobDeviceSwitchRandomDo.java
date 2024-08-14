package com.chason.common.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.chason.common.utils.TimeUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.PolicyService;
import com.chason.rwe.socket.SwitchDeviceMap;
import com.chason.rwe.utils.PolicyTimeUtils;
import com.chason.rwe.value.RandomFlagValue;

/**
 * 设置了随机策略的设备开机
 * */
@Component
public class JobDeviceSwitchRandomDo implements Job
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private PolicyService policyService;

    @Log("随机策略的设备开机")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        System.out.println("### start job:JobDeviceRandomSwitchDo");

        try
        {
            //白名单时间之内随机策略失效
            if(!isBetweenWhiteList())
            {
              //找出所有的随机策略
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("policyType", "随机策略");
                List<PolicyDO> randomPolicys = policyService.list(params);

                params = new HashMap<String, Object>();
                params.put("devStatus", 11);
                List<DeviceDO> devices = deviceService.list(params);

                List<DeviceDO> tmpDevices = new ArrayList<>();

                RandomFlagValue value = RandomFlagValue.getInstance();
                for (PolicyDO randomPolicy : randomPolicys)
                {
                    if(!isTiming(randomPolicy))
                    {
                        continue;
                    }

                    if(value.getActiveFlag().contains(randomPolicy.getPolicyId()))
                    {
                        continue;
                    }

                    for (DeviceDO device : devices)
                    {
                        if (!"自动".equals(device.getDevSwitchMode()))
                        {
                            continue;
                        }

                        if(StringUtils.isNotNull(device.getDevRandom()))
                        {
                            if(device.getDevRandom().indexOf(randomPolicy.getPolicyId()) != -1)
                            {
                                tmpDevices.add(device);
                            }
                        }
                    }

                    if(tmpDevices.size() > 4)
                    {
                        //从策略组中随机挑选2台设备开机
                        for(int i=0; i<2; i++)
                        {
                            DeviceDO theRandom = tmpDevices.get((int)(Math.random() * tmpDevices.size())); //随机挑选一台
                            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
                            List<String> arrPolicy = JSON.parseArray(theRandom.getDevPolicy(), String.class);
                            if (PolicyTimeUtils.isPolicyOpen(arrPolicy))
                            {
                                theSwitchDeviceMap.putCommandMap(theRandom.getDevNumber(), "SWITCHOPEN");
                            }
                        }
                    }
                    else
                    {
                        //少于4台全部开机
                        for (DeviceDO device : tmpDevices)
                        {
                            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
                            List<String> arrPolicy = JSON.parseArray(device.getDevPolicy(), String.class);
                            if (PolicyTimeUtils.isPolicyOpen(arrPolicy))
                            {
                                theSwitchDeviceMap.putCommandMap(device.getDevNumber(), "SWITCHOPEN");
                            }
                        }// end for
                    }// end if

                    value.getActiveFlag().add(randomPolicy.getPolicyId());
                    tmpDevices.clear();
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


    }

    /*private List<String> terminalNodes(List<String> keys)
    {
        HashSet<String> removeSet = new HashSet<>();

        for (String strKey : keys)
        {
            SpaceDO space = spaceService.get(strKey);
            if("-1".equals(strKey))
            {
                continue;
            }
            if(keys.contains(space.getSpaceParentId())) {
                removeSet.add(space.getSpaceParentId());
            }
        }
        List<String> tmp = remove(keys, removeSet);
        return tmp;
    }

    private List<String> remove(List<String> oldlist, HashSet<String> removeSet){
        List<String> newList = new ArrayList<>();
        for (String str : oldlist)
        {
            if(removeSet.contains(str))
            {
                continue;
            }
            newList.add(str);
        }
        return newList;
    }*/

    /**
     * 判断是不是当前随机策略的时间段
     * @param thePolicy
     * @return
     * @throws ParseException
     */
    private boolean isTiming(PolicyDO thePolicy)
    {
        SimpleDateFormat sdfdate     = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String strStartTime = sdfdate.format(nowTime) + " " + thePolicy.getPolicyStartTime();
        Date startTime;
        try
        {
            startTime = sdfFull.parse(strStartTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startTime);
            int minute = Integer.parseInt(thePolicy.getPolicyDur());
            cal.add(Calendar.MINUTE, minute);

            Date finishTime = cal.getTime();
            if (TimeUtils.isBetweenDate(startTime, finishTime))
            {
                return true;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前时间是否在白名单区间时间
     * @return
     * @throws ParseException
     */
    private boolean isBetweenWhiteList() throws ParseException
    {

        return false;
    }
}
