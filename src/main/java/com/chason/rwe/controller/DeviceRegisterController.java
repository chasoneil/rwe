package com.chason.rwe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chason.common.utils.EasyPoiUtils;
import com.chason.common.utils.FileType;
import com.chason.common.utils.GUIDUtils;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.value.DeviceFileValue;

/**
 * 设备注册
 * @author chenshen
 */
@Controller
@RequestMapping("/rtm/device/register")
public class DeviceRegisterController extends BaseController
{
    @Autowired
    private DeviceService deviceService;

    @GetMapping()
    @RequiresPermissions("rwe:device:register")
    String RegisterList()
    {
        return "rwe/device/register/device_register_list";
    }

    /**
     * 注册审核
     * @return
     */
    @GetMapping("/deviceCheckList")
    @RequiresPermissions("rwe:device:register")
    String checkList()
    {
        return "rwe/device/register/device_check_list";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        String keyWord = (String) params.get("keyWord");
        if(StringUtils.isNotNull(keyWord))
        {
            params.put("devCode", keyWord);
        }

        // 列出状态是11 / 10(正常)的设备
        Query query = new Query(params);
        List<DeviceDO> deviceList = deviceService.listValid(query);
        int total = deviceService.count(query);
        PageUtils pageUtils = new PageUtils(deviceList, total);
        return pageUtils;
    }

    /**
     * 新增设备-页面
     */
    @Log("新增设备")
    @GetMapping("/add")
    @RequiresPermissions("rwe:device:register")
    String add()
    {
        return "rwe/device/register/add";
    }

    /**
     * 新增设备-操作
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rwe:device:register")
    public R save(DeviceDO device)
    {
        int count;
        if(!StringUtils.isNotNull(device.getDevGroupCode()))
        {
            return R.error("设备编组必须要选哦~");
        }
        if(!StringUtils.isNotNull(device.getDevType()))
        {
            return R.error("设备类型一定要选哦~");
        }

        if (!StringUtils.isNotNull(device.getDevId())) // 新建信息
        {
            device.setDevId(GUIDUtils.generateGUID(device)); // 设置主键
            device.setDevStatus("11");
            device.setDevRegTime(new java.util.Date());
            device.setDevRegBy(super.getUsername());
            count = deviceService.save(device);
        }
        else // 保存修改信息
        {
            count = deviceService.update(device);
        }
        if (count > 0)
        {
            return R.ok().put("key", device.getDevId());
        }
        return R.error();
    }

    /**
     * 修改设备-页面
     * @param devId
     * @param model
     * @return
     */
    @Log("修改设备")
    @GetMapping("/edit/{devId}")
    @RequiresPermissions("rwe:device:register")
    String edit(@PathVariable("devId") String devId, Model model)
    {
        DeviceDO device = deviceService.get(devId);
        model.addAttribute("device", device);
        return "rwe/device/register/edit";
    }

    /**
     * 修改设备-操作
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("rwe:device:register")
    public R update(DeviceDO device)
    {
        if(!StringUtils.isNotNull(device.getDevGroupCode()))
        {
            return R.error("设备编组必须要选择哦~");
        }
        if(!StringUtils.isNotNull(device.getDevType()))
        {
            return R.error("终端类型必须要选择哦~");
        }
        String type = deviceService.getDevTypeByGroupCode(device.getDevGroupCode());
        if(StringUtils.isNotNull(type) && !type.equals(device.getDevType()))
        {
            return R.error("类型是 " + device.getDevType() + " 的设备不能分到类型是 " + type + " 的组中");
        }

        if(device.getDevRegTime() == null)
        {
            device.setDevRegTime(new java.util.Date());
        }
        if(!StringUtils.isNotNull(device.getDevRegBy()))
        {
            device.setDevRegBy(super.getUsername());
        }
        device.setDevStatus("11");
        deviceService.update(device);
        return R.ok();
    }

    /**
     * 删除设备
     */
    @Log("删除设备")
    @PostMapping("/remove")
    @RequiresPermissions("rwe:device:register")
    @ResponseBody
    public R remove(String devId)
    {
        if (deviceService.remove(devId) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 批量删除
     */
    @Log("批量删除设备信息")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("rwe:device:register")
    public R remove(@RequestParam("ids[]") String[] devIds)
    {
        deviceService.batchRemove(devIds);
        return R.ok();
    }

    /**
     * 批量导入设备-页面
     */
    @Log("批量导入设备")
    @GetMapping("/devImport")
    @RequiresPermissions("rwe:device:register")
    String devImport()
    {
        return "rwe/device/register/import";
    }

    /**
     * 批量导入设备-操作
     * @param file 获得的文件
     * */
    @ResponseBody
    @PostMapping(value = "/importDo")
    @RequiresPermissions("rwe:device:register")
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
            //列出所有的设备
            List<DeviceDO> devices = deviceService.list(params);

            Map<String, DeviceDO> mDevices = new HashMap<String, DeviceDO>();
            for (DeviceDO theDevice : devices)
            {
                mDevices.put(theDevice.getDevCode(), theDevice);
            }

            for (DeviceFileValue theValue : deviceFileValueList)
            {
                DeviceDO theDevice = mDevices.get(theValue.getDevCode());
                if (theDevice == null)  //系统不存在该设备
                {
                    theDevice = new DeviceDO();
                    String errMsg = "";
                    if(StringUtils.isNotNull(errMsg))
                    {
                        return R.error("表格信息有误");
                    }
                    theDevice.setDevId(GUIDUtils.generateGUID(theDevice));
                    theDevice.setDevCode(theValue.getDevCode());
                    theDevice.setDevNumber(theValue.getDevNumber());
                    theDevice.setDevGroupCode(theValue.getDevGroupCode());
                    theDevice.setDevType(theValue.getDevType());
                    theDevice.setDevStatus("11");
                    theDevice.setDevRegBy(super.getUsername());
                    theDevice.setDevRegTime(new java.util.Date());
                    deviceService.save(theDevice);  //保存
                }

                if("0".equals(theDevice.getDevStatus()))  //设备已存在（禁用状态）
                {
                    theDevice.setDevStatus("11");
                    deviceService.update(theDevice);  //更新
                }
            }
        }
        catch (Exception e)
        {
            return R.error("服务器错误...");
        }
        return R.ok();
    }

    /**
     * 历史注册-页面
     * @param params
     * @return
     */
    @Log("查看历史注册")
    @ResponseBody
    @GetMapping("/historyList")
    public PageUtils historyList(@RequestParam Map<String, Object> params)
    {
        // 查询列表数据
        Query query = new Query(params);
        List<DeviceDO> devTmps = deviceService.list(query);
        List<DeviceDO> deviceList = new ArrayList<DeviceDO>();
        for (DeviceDO theDevice : devTmps)  //设备状态为1 且设备注册时间不为空
        {
            if(theDevice.getDevRegTime() != null)
            {
                deviceList.add(theDevice);
            }
        }
        int total = deviceList.size();
        PageUtils pageUtils = new PageUtils(deviceList, total);
        return pageUtils;
    }

    /**
     * 批量审核-页面
     * @param devIds
     * @param model
     * @return
     */
    @GetMapping("/checkBatch/{ids}")
    @RequiresPermissions("rwe:device:register")
    String checkBatch(@PathVariable("ids") String[] devIds, Model model)
    {
        String strDevIds = "";
        for (String devId : devIds)
        {
            strDevIds += devId + ",";
        }
        model.addAttribute("devIds", strDevIds);
        return "rwe/device/register/check_batch";
    }

    /**
     * 批量审核-操作
     */
    @Log("批量审核设备")
    @PostMapping("/checkBatchDo")
    @RequiresPermissions("rwe:device:register")
    @ResponseBody
    public R checkBatchDo(@RequestParam Map<String, Object> params) {

        String strDevIds = (String) params.get("devIds");
        String[] devIds = strDevIds.split(",");

        for (String theDevId : devIds)
        {
            DeviceDO theDevice = deviceService.get(theDevId);
            if (theDevice == null)
            {
                return R.error("设备：" + theDevId + "不存在！");
            }

            if("00".equals(theDevice.getDevStatus()))
            {
                theDevice.setDevStatus("11");
                if(theDevice.getDevRegTime() == null)
                {
                    theDevice.setDevRegTime(new java.util.Date());
                }
                if(!StringUtils.isNotNull(theDevice.getDevRegBy()))
                {
                    theDevice.setDevRegBy(super.getUsername());
                }
            }
            theDevice.setDevGroupCode((String) params.get("devGroupCode"));
            theDevice.setDevType((String) params.get("devType"));
            deviceService.update(theDevice);
        }
        return R.ok();
    }
}
