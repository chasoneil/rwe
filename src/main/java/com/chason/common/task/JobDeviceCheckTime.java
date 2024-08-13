package com.chason.common.task;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chason.common.annotation.Log;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.socket.SwitchDeviceMap;

/**
 * 延时设置
 * */
@Component
public class JobDeviceCheckTime implements Job
{
    @Autowired
    private DeviceService deviceService;

    @Log("开始延时设置")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        //System.out.println("### start job:JobDeviceCheckTime");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("devStatus", "11");
        List<DeviceDO> devices = deviceService.list(params);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String sysTime = sdf.format(new java.util.Date());

        for (DeviceDO theDevice : devices)
        {
            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SYNCCLOCKTIME:"+sysTime);

            try
            {
                Thread.sleep(5000); //延迟5秒让设备上报心跳数据执行cmd
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "DELAYEDOPEN");              //延时功能开

            try
            {
                Thread.sleep(5000); //延迟5秒让设备上报心跳数据执行cmd
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "DELAYEDOPENTIME:01");       //设置延时时长
        }
    }
}
