package com.chason.rwe.controller;

import java.util.*;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.*;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.SpaceService;
import com.chason.rwe.socket.SwitchDeviceMap;
import com.chason.rwe.value.DeviceFileValue;

/**
 * 设备分组定时控制
 */

@Controller
@RequestMapping("/rtm/device/policy")
public class DevicePolicyController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;

    @GetMapping()
    @RequiresPermissions("rtm:device:policy")
    String Policy()
    {
        return "rwe/device/policy/device";
    }

    @Log("查看设备信息")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rtm:device:policy")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        if("noGroup".equals(params.get("devGroupCode")))
        {
            params.put("devGroupCode", "");
        }
        params.put("devStatus", "11");

        Query query = new Query(params);
        List<DeviceDO> deviceList = deviceService.list(query);
        int total = deviceService.count(query);
        PageUtils pageUtils = new PageUtils(deviceList, total);
        return pageUtils;
    }

    @GetMapping("/timeSet")
    @RequiresPermissions("rtm:device:policy")
    public String timeSet(Model model)
    {
        List<SpaceDO> spaces = spaceService.listByLikeManager(super.getUser());
        model.addAttribute("spaces", spaces);
        return "rtm/device/policy/timeSet";
    }

    @Log("设置设备定时信息")
    @PostMapping("/timeSetDo")
    @RequiresPermissions("rtm:device:policy")
    @ResponseBody
    public R timeSetDo(@RequestParam Map<String, Object> params)
    {
        String groupCodes = (String) params.get("groupCodes"); //进行操作的设备组号（多组）
        String timingFlag = (String) params.get("timingFlag");
        String openCommandTmp = (String) params.get("timingOpenTime");
        String closeCommmandTmp = (String) params.get("timingCloseTime");
        if("00".equals(timingFlag) && (StringUtils.isNotNull(openCommandTmp) || StringUtils.isNotNull(closeCommmandTmp)))
        {
            return R.error("开启定时开关才能设置定时开关时间哦~");
        }
        String openCommand = openCommandTmp.replace(":", ""); //定时开机时间 HHmm
        @SuppressWarnings("unused")
        String closeCommmand = closeCommmandTmp.replace(":", ""); //定时关机时间 HHmm
        String delayFlag = (String) params.get("delayFlag");
        String delayedOpenTime = (String) params.get("delayedOpenTime");
        if("00".equals(delayFlag) && StringUtils.isNotNull(delayedOpenTime))
        {
            return R.error("开始延时开关才能设置延时关闭时间哦~");
        }

        String[] strGroupCode = groupCodes.split(",");
        for (String groupCode : strGroupCode)
        {
            List<DeviceDO> devices = deviceService.findAllValidByGroupCode(groupCode);
            for (DeviceDO theDevice : devices)
            {
                SwitchDeviceMap theSwitchDeviceMap = SwitchDeviceMap.getInstance();
                List<String> commandTmp = new ArrayList<String>();
                if("11".equals(timingFlag))
                {
                    commandTmp.add("TIMINGOPEN");
                    commandTmp.add("TIMINGOPENTIME:" + openCommand);
                    //commandTmp.add("TIMINGCLOSETIME:" + closeCommmand);
                }
                if("11".equals(delayFlag))
                {
                    commandTmp.add("DELAYEDOPEN");
                    commandTmp.add("DELAYEDOPENTIME:" + delayedOpenTime);
                }
                System.out.println("json:" + theDevice.getDevNumber() + " | " + JSON.toJSONString(commandTmp));
                theSwitchDeviceMap.putCommandMap(theDevice.getDevNumber(), JSON.toJSONString(commandTmp));
            }
        }
        return R.ok();
    }



    /**
     * 批量分组-操作
     */
    @Log("修改设备分组信息")
    @PostMapping("/groupDo")
    @RequiresPermissions("rtm:device:group")
    @ResponseBody
    public R groupDo(@RequestParam Map<String, Object> params)
    {
        String devIds = (String) params.get("devIds");
        String[] strDevIds = devIds.split(",");
        String type = deviceService.getDevTypeByGroupCode((String) params.get("devGroupCode"));
        for (String devId : strDevIds)
        {
            DeviceDO theDevice = deviceService.get(devId);
            if(theDevice == null)
            {
                return R.error("不存在设备:" + devId);
            }
            if(StringUtils.isNotNull(type) && !theDevice.getDevType().equals(type))
            {
                return R.error(theDevice.getDevCode() + ":设备类型和分组的设备类型不符,请重新选择!");
            }
            theDevice.setDevGroupCode((String) params.get("devGroupCode"));
            if("00".equals(theDevice.getDevStatus()))
            {
                theDevice.setDevStatus("11");
            }
            deviceService.update(theDevice);
        }
        return R.ok();
    }

    /**
     * 导入设备分组文件-页面
     */
    @Log("导入设备分组文件")
    @GetMapping("/groupFileImp")
    @RequiresPermissions("rtm:device:group")
    String groupImp()
    {
        return "rwe/device/group/import";
    }

    /**
     * 导入设备分组文件-操作
     * @param file 获得的文件
     * */
    @ResponseBody
    @PostMapping(value = "/importDo")
    @RequiresPermissions("rtm:device:group")
    public R importDo(@RequestPart("filesd") MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        if (!FileType.isValidType(fileName, "xls") && !FileType.isValidType(fileName, "xlsx"))
        {
            return R.error("请上传Excel文件！");
        }

        try
        {
            //处理excel
            List<DeviceFileValue> deviceFileValueList = EasyPoiUtils.importExcel(file, 0, 1, DeviceFileValue.class);
            if (deviceFileValueList.size() == 0)
            {
                return R.error("上传文件数据为空！");
            }

            Map<String, Object> params = new HashMap<>(16);
            List<DeviceDO> devices = deviceService.list(params);

            Map<String, DeviceDO> mDevices = new HashMap<String, DeviceDO>();
            for (DeviceDO theDevice : devices)
            {
                mDevices.put(theDevice.getDevCode(), theDevice);
            }

            for (DeviceFileValue theValue : deviceFileValueList)
            {
                DeviceDO theDevice = mDevices.get(theValue.getDevCode());
                if (theDevice != null)
                {
                    deviceService.update(theDevice);  //更新
                }
            }
        }
        catch (Exception e)
        {
            return R.error("分组文件保存错误");
        }
        return R.ok();
    }
}
