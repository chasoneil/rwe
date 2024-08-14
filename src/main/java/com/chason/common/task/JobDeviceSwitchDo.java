package com.chason.common.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.chason.rwe.service.DeviceService;

import com.chason.rwe.socket.SwitchDeviceMap;
import com.chason.rwe.utils.PolicyTimeUtils;

/**
 * 根据设备的运行策略
 * 定时给设备发送开机或关机命令
 * */
@Component
public class JobDeviceSwitchDo implements Job
{
    @Autowired
    private DeviceService deviceService;



    @Log("设备任务")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        System.out.println("### start job:JobDeviceSwitchDo");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("devStatus", 11);
        List<DeviceDO> devices = deviceService.list(params);

        for (DeviceDO theDevice : devices)
        {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -2);
            if (theDevice.getDevOnlineLastTime() == null || theDevice.getDevOnlineLastTime().before(cal.getTime()))
            {
                theDevice.setDevStatus("10");
                deviceService.update(theDevice); //心跳数据获取后，可以刷新为11
                continue;
            }

            if ("手动".equals(theDevice.getDevSwitchMode()))
            {
                cal.add(Calendar.MINUTE, -60);
                if(theDevice.getDevSwitchHandTime() != null)
                {
                    if (cal.getTime().after(theDevice.getDevSwitchHandTime()))
                    {
                        theDevice.setDevSwitchMode("自动");
                        deviceService.update(theDevice);
                    }
                }
            }

            if (!"自动".equals(theDevice.getDevSwitchMode())) {
                continue;
            }

            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();

            //在白名单时间中

            if(isBetweenWhiteList()) {
                theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHCLOSE"); //默认关
                continue;
            }


            if (theDevice.getDevPolicy() == null || "-".equals(theDevice.getDevPolicy())) {
                theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHCLOSE"); //默认关
                continue;
            }

            List<String> arrPolicy = JSON.parseArray(theDevice.getDevPolicy(), String.class);
            if (PolicyTimeUtils.isPolicyOpen(arrPolicy)) {
                //应用于随机策略的过滤掉
                if(StringUtils.isNotNull(theDevice.getDevRandom())) {
                    continue;
                }

                theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHOPEN");
            }
            else {
                theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHCLOSE");
            }//end if

        }//end for
    }

    /**
     * 当前时间是否在白名单区间时间
     * @return
     * @throws ParseException
     */
    private boolean isBetweenWhiteList() {
        return false;
    }
}
