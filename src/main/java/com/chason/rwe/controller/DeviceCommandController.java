package com.chason.rwe.controller;

import java.util.*;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.*;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.SpaceService;
import com.chason.rwe.socket.SwitchDeviceMap;
import com.chason.rwe.socket.SwitchDeviceValue;

/**
 * 设备控制
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */

@Controller
@RequestMapping("/rtm/device/command")
public class DeviceCommandController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;

    @GetMapping()
    @RequiresPermissions("rwe:device:command")
    String Device()
    {
        return "rwe/device/command/device";
    }

    @Log("查看设备信息")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rwe:device:command")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        String spaceId = (String)params.get("spaceId");
        if(StringUtils.isNotNull(spaceId))
        {
            SpaceDO theSpace = spaceService.get(spaceId);
            params.put("devGroupCode", theSpace.getSpaceCode());
        }
        Query query = new Query(params);
        List<DeviceDO> devices = deviceService.listValid(query);
        SwitchDeviceMap theSwitchMap = SwitchDeviceMap.getInstance();
        Map<String, SwitchDeviceValue> mSwitchValues = theSwitchMap.getSwitchDeviceValueMap();
        List<DeviceInfoValue> results = new ArrayList<DeviceInfoValue>();
        for (DeviceDO theDevice : devices)
        {
            DeviceInfoValue theValue = new DeviceInfoValue();
            String spaceCodes = theDevice.getDevGroupCode();
            if(StringUtils.isNotNull(spaceCodes))
            {
                String code = spaceCodes.split(",")[0];
                SpaceDO theSpace = spaceService.findByCode(code);
                theDevice.setDevGroupCode(theSpace.getSpaceAddress());
            }
            theValue.setDeviceInfo(theDevice);
            SwitchDeviceValue theSwitchValue = mSwitchValues.get(theDevice.getDevNumber());
            if (theSwitchValue == null)
            {
                theSwitchValue = new SwitchDeviceValue();
            }

            theValue.setSwitchInfo(theSwitchValue);
            results.add(theValue);
        }
        int total = deviceService.count(query);
        PageUtils pageUtils = new PageUtils(results, total);
        return pageUtils;
    }

    /**
     * 设备通电
     */
    @Log("设备通电")
    @PostMapping("/switchOpen")
    @ResponseBody
    @RequiresPermissions("rwe:device:command")
    public R switchOpen(@RequestParam Map<String, Object> params)
    {
        String devIds = (String) params.get("devIds");
        String[] strDevIds = devIds.split(",");
        for (String devId : strDevIds)
        {
            if (!StringUtils.isNotNull(devId))
            {
                continue;
            }

            DeviceDO theDevice = deviceService.get(devId);
            if (theDevice == null)
            {
                return R.error("无法找到设备！");
            }

            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHOPEN");
        }

        return R.ok();
    }

    /**
     * 设备断电
     */
    @Log("设备断电")
    @PostMapping("/switchClose")
    @ResponseBody
    @RequiresPermissions("rwe:device:command")
    public R switchClose(@RequestParam Map<String, Object> params)
    {
        String devIds = (String) params.get("devIds");
        String[] strDevIds = devIds.split(",");
        for (String devId : strDevIds)
        {
            if (!StringUtils.isNotNull(devId))
            {
                continue;
            }

            DeviceDO theDevice = deviceService.get(devId);
            if (theDevice == null)
            {
                return R.error("无法找到设备！");
            }

            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "SWITCHCLOSE");
        }
        return R.ok();
    }

    /**
     * 设备重启
     */
    @Log("设备重启")
    @PostMapping("/reboot")
    @ResponseBody
    @RequiresPermissions("rwe:device:command")
    public R reboot(@RequestParam Map<String, Object> params)
    {
        String devIds = (String) params.get("devIds");
        String[] strDevIds = devIds.split(",");
        for (String devId : strDevIds)
        {
            if (!StringUtils.isNotNull(devId))
            {
                continue;
            }

            DeviceDO theDevice = deviceService.get(devId);
            if (theDevice == null)
            {
                return R.error("无法找到设备！");
            }

            SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
            theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), "REBOOT");
        }

        return R.ok();
    }

    /**
     * 设备通电方式切换
     */
    @Log("设备通电方式切换")
    @PostMapping("/switchModeChange")
    @ResponseBody
    @RequiresPermissions("rwe:device:command")
    public R switchModeChange(@RequestParam Map<String, Object> params)
    {
        String devIds = (String) params.get("devIds");
        String[] strDevIds = devIds.split(",");
        for (String devId : strDevIds)
        {
            if (!StringUtils.isNotNull(devId))
            {
                continue;
            }

            DeviceDO theDevice = deviceService.get(devId);
            if (theDevice == null)
            {
                return R.error("无法找到设备！");
            }

            String strMode = "自动";
            Date handTime = null;
            if ("自动".equals(theDevice.getDevSwitchMode()))
            {
                strMode = "手动";
                handTime = new java.util.Date();
            }
            theDevice.setDevSwitchMode(strMode);
            theDevice.setDevSwitchHandTime(handTime);
            deviceService.update(theDevice);
        }

        return R.ok();
    }

    class DeviceInfoValue
    {
        public String getDevId()
        {
            return this.getDeviceInfo().getDevId();
        }

        private DeviceDO deviceInfo;

        private SwitchDeviceValue switchInfo;

        public DeviceDO getDeviceInfo()
        {
            return deviceInfo;
        }

        public void setDeviceInfo(DeviceDO deviceInfo)
        {
            this.deviceInfo = deviceInfo;
        }

        public SwitchDeviceValue getSwitchInfo()
        {
            return switchInfo;
        }

        public void setSwitchInfo(SwitchDeviceValue switchInfo)
        {
            this.switchInfo = switchInfo;
        }

        public String getDevicePrompt()
        {
            if (this.getSwitchInfo() != null)
            {
                String strSwitch = this.getSwitchInfo().getSwitch();
                String strCurrent = this.getSwitchInfo().getCurrent();

                if ("10".equals(strSwitch)) //已通电
                {
                    String strResult = "已上电";
                    if ("00".equals(strCurrent))
                    {
                        strResult += "，但负载电流太低";
                    }
                    return strResult;
                }
                else if ("00".equals(strSwitch)) //未通电
                {
                    return "未上电";
                }
            }
            return "-";
        }
    }
}
