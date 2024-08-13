package com.chason.rwe.controller;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.IPUtils;
import com.chason.common.utils.R;
import com.chason.rwe.controller.finder.DeviceFinderThread;
import com.chason.rwe.controller.finder.IpInfoInstance;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.service.DeviceService;

/**
 * 找到设备
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */

@Controller
@RequestMapping("/rtm/device/finder")
public class DeviceFinderController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @Log("发现设备")
    @GetMapping()
    @ResponseBody
    @RequiresPermissions("rwe:device:register")
    public R finder()
    {
        System.out.println("##### 查找小乙网控设备 #####");
        IpInfoInstance.INSTANCE.cleanAll();

        List<String> ips = IPUtils.getLocalIPList();

        ExecutorService executor = Executors.newFixedThreadPool(16);

        for (String ip : ips)
        {
            if ("127.0.0.1".equals(ip))
            {
                continue;
            }

            //异步调用
            CompletableFuture<String> doSubsystem = CompletableFuture.supplyAsync(() ->
            {
                String tip = ip.substring(0, ip.lastIndexOf(".")) + ".255";
                System.out.println("###ip:" + tip);

                DeviceFinderThread theFindDev = new DeviceFinderThread();
                theFindDev.setIp(tip);

                Thread t = new Thread(theFindDev);
                t.run();

                return "成功发现设备，关闭端口 ->";
            }, executor);
            doSubsystem.thenAcceptAsync((e)->{System.out.println("执行情况：" + e);});
        }
        return R.ok("扫描执行中，稍后查看设备列表");
    }

    @Log("查看设备信息")
    @GetMapping("/list")
    @RequiresPermissions("rwe:device:register")
    public String list(Model model)
    {
        Map<String, String> mIPs = IpInfoInstance.INSTANCE.getAll();
        List<DeviceDO> devices = deviceService.list(new HashMap<String, Object>());
        List<Device> theDevices = new ArrayList<>();
        for (DeviceDO theDevice : devices)
        {
            if (mIPs.containsKey(theDevice.getDevCode()))
            {
                Device device = new Device();
                device.setDevNumber(theDevice.getDevNumber());
                device.setIp(theDevice.getDevCode());
                device.setStatus(theDevice.getDevStatus());
                mIPs.put(theDevice.getDevCode(), theDevice.getDevNumber());
                theDevices.add(device);
            }
        }
        model.addAttribute("ips", mIPs);
        model.addAttribute("devices", theDevices);
        return "rtm/device/finder/ips";
    }
}

class Device
{
    private String ip;

    private String devNumber;

    private String status;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getDevNumber()
    {
        return devNumber;
    }

    public void setDevNumber(String devNumber)
    {
        this.devNumber = devNumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
