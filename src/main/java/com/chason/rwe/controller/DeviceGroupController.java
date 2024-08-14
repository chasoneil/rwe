package com.chason.rwe.controller;

import java.util.*;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.*;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.domain.SpaceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.service.SpaceService;
import com.chason.rwe.value.DeviceFileValue;

/**
 * 终端设备分组

 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */

@Controller
@RequestMapping("/rtm/device/group")
public class DeviceGroupController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SpaceService spaceService;

    @GetMapping()
    @RequiresPermissions("rtm:device:group")
    String Device()
    {
        return "rwe/device/group/device";
    }

    @Log("查看设备信息")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("rtm:device:group")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        String spaceId = (String) params.get("spaceId");
        if(StringUtils.isNotNull(spaceId))
        {
            SpaceDO theSpace = spaceService.get(spaceId);
            params.put("devGroupCode", theSpace.getSpaceCode());
        }
        String keyWord = (String) params.get("keyWord");
        if(StringUtils.isNotNull(keyWord))
        {
            params.put("devCode", keyWord);
        }
        Query query = new Query(params);
        List<DeviceDO> deviceList = deviceService.listValid(query);
        int total = deviceService.count(query);
        PageUtils pageUtils = new PageUtils(deviceList, total);
        return pageUtils;
    }

    @ResponseBody
    @GetMapping("/detail")
    public DeviceDO detail(@RequestParam Map<String, Object> params)
    {
        String devId = (String) params.get("devId");
        if(StringUtils.isNotNull(devId))
        {
            DeviceDO theDevice = deviceService.get(devId);
            if(StringUtils.isNotNull(theDevice.getDevGroupCode()))
            {
                String code = theDevice.getDevGroupCode().split(",")[0];
                SpaceDO theSpace = spaceService.findByCode(code);
                theDevice.setDevGroupCode(theSpace.getSpaceAddress());
                return theDevice;
            }
        }
        return new DeviceDO();
    }

    /**
     * 个别分组-页面（合用一个提交）
     * */
    @GetMapping("/edit/{devId}")
    @RequiresPermissions("rtm:device:group")
    String edit(@PathVariable("devId") String devId, Model model)
    {
        DeviceDO device = deviceService.get(devId);
        model.addAttribute("devIds", device.getDevId());
        Map<String, Object> param = new HashMap<>();
        List<SpaceDO> spaces = spaceService.list(param);
        model.addAttribute("spaces", spaces);
        return "rwe/device/group/edit";
    }

    /**
     * 批量分组-页面（合用一个提交）
     * @param devIds
     * @param model
     * @return
     */
    @GetMapping("/groupEdit/{ids}")
    @RequiresPermissions("rtm:device:group")
    String groupEdit(@PathVariable("ids") String[] devIds, Model model)
    {
        String strDevIds = "";
        for (String devId : devIds)
        {
            strDevIds += devId + ",";
        }

        Map<String, Object> param = new HashMap<>();
        List<SpaceDO> spaces = spaceService.list(param);
        model.addAttribute("spaces", spaces);
        model.addAttribute("devIds", strDevIds);
        return "rwe/device/group/edit";
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
        String spaceId = (String) params.get("spaceId");
        SpaceDO theSpace = spaceService.get(spaceId);
        String type = deviceService.getDevTypeByGroupCode(theSpace.getSpaceCode());
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
            theDevice.setDevGroupCode(this.getCodes(spaceId));
            if("00".equals(theDevice.getDevStatus()))
            {
                theDevice.setDevStatus("11");
            }
            deviceService.update(theDevice);
        }
        return R.ok();
    }

    private String getCodes(String spaceId)
    {
        String codes = "";
        SpaceDO theSpace = spaceService.get(spaceId);
        while(StringUtils.isNotNull(theSpace.getSpaceParentId()))
        {
            codes += (theSpace.getSpaceCode() + ",");
            theSpace = spaceService.get(theSpace.getSpaceParentId());
        }
        codes += theSpace.getSpaceCode();
        return codes;
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
