package com.chason.rwe.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.SpaceService;
import com.chason.rwe.socket.SwitchDeviceMap;
import com.chason.rwe.socket.SwitchDeviceValue;
import com.chason.rwe.value.DevicePolicyValue;



/**
 * 获取主页信息的controller
 * @author chason
 *
 */
@Controller
@RequestMapping("/main")
public class MainController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;

    @Log("访问主页")
    @GetMapping("/index")
    String main()
    {
        return "rwe/main/main";
    }

    /**
     * 列出首页设备数据
     * @return
     */
    @GetMapping("/device")
    String deviceList(@RequestParam Map<String, Object> param, Model model)
    {
        String spaceId = (String) param.get("spaceId");
        if(!"root".equals(spaceId))
        {
            SpaceDO theSpace = spaceService.get(spaceId);
            param.put("devGroupCode", theSpace.getSpaceCode());
        }

        //获取所有的有效设备
        List<DeviceDO> devices = deviceService.listValid(param);
        List<DeviceStat> devStats = new ArrayList<>();
        //获取所有设备状态的单例map
        SwitchDeviceMap theSwitchMap = SwitchDeviceMap.getInstance();
        Map<String, SwitchDeviceValue> switchDeviceValueMap = theSwitchMap.getSwitchDeviceValueMap();
        Map<String, String> spaceMsg = new HashMap<>(); //空间信息的map k:空间编组号 v:空间名称

        Map<String, Object> map = new HashMap<>();
        List<SpaceDO> spaces = spaceService.list(map);
        for (SpaceDO theSpace : spaces)
        {
            spaceMsg.put(theSpace.getSpaceCode(), theSpace.getSpaceAddress());
        }

        for (DeviceDO theDevice : devices)
        {
            DeviceStat theStat = new DeviceStat();
            theStat.setDevice(theDevice);
            SwitchDeviceValue switchDeviceValue = switchDeviceValueMap.get(theDevice.getDevNumber());
            theStat.setDevFlag("01");  //初始化设备状态已离线
            theStat.setSwitchFlag("00");
            theStat.setCurrentFlag("00");
            if(switchDeviceValue != null)
            {
                theStat.setSwitchFlag(switchDeviceValue.getSwitch());
                theStat.setCurrentFlag(switchDeviceValue.getCurrent());
            }
            if("11".equals(theDevice.getDevStatus()))
            {
                theStat.setDevFlag("11");
            }
            theStat.setSpaceAddress("");
            String codes = theDevice.getDevGroupCode();
            if(StringUtils.isNotNull(codes))
            {
                String[] strCodes = codes.split(",");
                String address = spaceMsg.get(strCodes[0]);
                if(StringUtils.isNotNull(address))
                {
                    theStat.setSpaceAddress(address);
                }
            }
            theStat.setTimer("NOTIMER");
            if(StringUtils.isNotNull(theDevice.getDevPolicy()) && !"-".equals(theDevice.getDevPolicy()))
            {
                theStat.setTimer("策略运行中");
            }
            devStats.add(theStat);
        }
        model.addAttribute("devices", devStats);
        model.addAttribute("spaceId", param.get("spaceId"));
        return "rwe/main/device";
    }

    @GetMapping("/deviceCount")
    String deviceCount(Model model)
    {
        DeviceCount theDeviceCount = new DeviceCount();
        Map<String, Object> param = new HashMap<>();
        List<DeviceDO> allDevices = deviceService.list(param); //列出所有的设备（已审核和未审核）
        int devOn = 0,devWorking = 0,devTimer = 0,devCheck = 0;
        if(allDevices == null)
        {
            theDeviceCount.setDevAmount(0);
            theDeviceCount.setDevCheck(devCheck);
            theDeviceCount.setDevOn(devOn);
            theDeviceCount.setDevTimer(devTimer);
            theDeviceCount.setDevWorking(devWorking);
            model.addAttribute("devCount", theDeviceCount);
            return "rwe/main/count";
        }

        for (DeviceDO theDevice : allDevices)
        {
            if("00".equals(theDevice.getDevStatus()))
            {
                devCheck ++;
            }

            if(StringUtils.isNotNull(theDevice.getDevPolicy()) && !"-".equals(theDevice.getDevPolicy()))
            {
                devTimer ++;
            }

            SwitchDeviceMap instance = SwitchDeviceMap.getInstance();
            Map<String, SwitchDeviceValue> switchDeviceValueMap = instance.getSwitchDeviceValueMap();
            if(switchDeviceValueMap != null)
            {
                SwitchDeviceValue switchDeviceValue = switchDeviceValueMap.get(theDevice.getDevNumber());
                if(switchDeviceValue != null && "11".equals(theDevice.getDevStatus()))
                {
                    devOn ++;
                }

                if(switchDeviceValue != null && "10".equals(switchDeviceValue.getCurrent()))
                {
                    devWorking ++;
                }
            }
        }
        theDeviceCount.setDevAmount(allDevices.size());
        theDeviceCount.setDevCheck(devCheck);
        theDeviceCount.setDevOn(devOn);
        theDeviceCount.setDevTimer(devTimer);
        theDeviceCount.setDevWorking(devWorking);
        model.addAttribute("devCount", theDeviceCount);

        return "rwe/main/count";
    }

    @GetMapping("/timer")
    String timer(Model model)
    {
        DeviceTimer theDeviceTimer = new DeviceTimer();
        Map<String, Integer> timer = new HashMap<>();
        Map<String, Integer> delay = new HashMap<>();

        List<DeviceDO> devices = deviceService.findAllByStatus("11");
        for (DeviceDO theDevice : devices)
        {
            if(StringUtils.isNotNull(theDevice.getDevPolicy()))
            {
                DevicePolicyValue thePolicy = JSON.parseObject(theDevice.getDevPolicy(), DevicePolicyValue.class);
                if(thePolicy.isDelayedFlag())
                {
                    if(delay.get(thePolicy.getDelayedOpenTime()) == 0)
                    {
                        delay.put(thePolicy.getDelayedOpenTime(), 1);
                    }
                    else
                    {
                        Integer devNum = delay.get(thePolicy.getDelayedOpenTime());
                        devNum ++;
                    }
                }
                if(thePolicy.isTimingFlag())
                {
                    if(timer.get(thePolicy.getTimingOpenTime()) == 0)
                    {
                        timer.put(thePolicy.getTimingOpenTime(), 1);
                    }
                    else
                    {
                        Integer devNum = timer.get(thePolicy.getTimingOpenTime());
                        devNum ++;
                    }
                }
            }
        }
        theDeviceTimer.setDelay(delay);
        theDeviceTimer.setTimer(timer);
        model.addAttribute("timer", theDeviceTimer);
        return "rwe/main/timer";
    }

    /**
     * 首页设备列表的包装类
     * @author 12831
     */
    class DeviceStat
    {
        //当前设备
        private DeviceDO device;

        /*
         * 00 新发现
         * 01 已离线
         * 11 联机中
         */
        private String devFlag;

        //所属的空间名
        private String spaceAddress;

        //设备通电状态
        private String switchFlag;

        //设备负载状态
        private String currentFlag;

        //定时标志
        private String timer;

        public DeviceDO getDevice()
        {
            return device;
        }

        public void setDevice(DeviceDO device)
        {
            this.device = device;
        }

        public String getDevFlag()
        {
            return devFlag;
        }

        public void setDevFlag(String devFlag)
        {
            this.devFlag = devFlag;
        }

        public String getSpaceAddress()
        {
            return spaceAddress;
        }

        public void setSpaceAddress(String spaceAddress)
        {
            this.spaceAddress = spaceAddress;
        }

        public String getSwitchFlag()
        {
            return switchFlag;
        }

        public void setSwitchFlag(String switchFlag)
        {
            this.switchFlag = switchFlag;
        }

        public String getCurrentFlag()
        {
            return currentFlag;
        }

        public void setCurrentFlag(String currentFlag)
        {
            this.currentFlag = currentFlag;
        }

        public String getTimer()
        {
            return timer;
        }

        public void setTimer(String timer)
        {
            this.timer = timer;
        }
    }

    /**
     * 设备统计的包装类
     * @author 12831
     *
     */
    class DeviceCount
    {
        private int devAmount; //设备总数

        private int devOn; //已联机设备

        private int devWorking; //工作中的设备

        private int devTimer; //定时的设备

        private int devCheck; //待审核的设备

        public int getDevAmount()
        {
            return devAmount;
        }

        public void setDevAmount(int devAmount)
        {
            this.devAmount = devAmount;
        }

        public int getDevOn()
        {
            return devOn;
        }

        public void setDevOn(int devOn)
        {
            this.devOn = devOn;
        }

        public int getDevWorking()
        {
            return devWorking;
        }

        public void setDevWorking(int devWorking)
        {
            this.devWorking = devWorking;
        }

        public int getDevTimer()
        {
            return devTimer;
        }

        public void setDevTimer(int devTimer)
        {
            this.devTimer = devTimer;
        }

        public int getDevCheck()
        {
            return devCheck;
        }

        public void setDevCheck(int devCheck)
        {
            this.devCheck = devCheck;
        }
    }

    /**
     * 设备时间情况的包装类
     * @author 12831
     */
    class DeviceTimer
    {
        private Map<String, Integer> timer; //定时设备的map k:时间  v:这个时间的设备数量

        private Map<String, Integer> delay; //延时设备的map k:时间  v:这个时间的设备数量

        public Map<String, Integer> getTimer()
        {
            return timer;
        }

        public void setTimer(Map<String, Integer> timer)
        {
            this.timer = timer;
        }

        public Map<String, Integer> getDelay()
        {
            return delay;
        }

        public void setDelay(Map<String, Integer> delay)
        {
            this.delay = delay;
        }
    }
}
