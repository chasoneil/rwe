package com.chason.common.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.chason.common.config.RtmdoConfig;
import com.chason.common.domain.FileDO;
import com.chason.common.service.FileService;
import com.chason.common.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 */
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController
{

    @Autowired
    private FileService sysFileService;

    @Autowired
    private RtmdoConfig rtmdoConfig;

    @GetMapping()
    @RequiresPermissions("common:sysFile:sysFile")
    String sysFile(Model model)
    {
        // Map<String, Object> params = new HashMap<>(16);
        return "common/file/file";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("common:sysFile:sysFile")
    public PageUtils list(@RequestParam Map<String, Object> params)
    {
        // 查询列表数据
        Query query = new Query(params);
        List<FileDO> sysFileList = sysFileService.list(query);
        int total = sysFileService.count(query);
        PageUtils pageUtils = new PageUtils(sysFileList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    // @RequiresPermissions("common:bComments")
    String add()
    {
        return "common/sysFile/add";
    }

    @GetMapping("/edit")
    // @RequiresPermissions("common:bComments")
    String edit(Long id, Model model)
    {
        FileDO sysFile = sysFileService.get(id);
        model.addAttribute("sysFile", sysFile);
        return "common/sysFile/edit";
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("common:info")
    public R info(@PathVariable("id") Long id)
    {
        FileDO sysFile = sysFileService.get(id);
        return R.ok().put("sysFile", sysFile);
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("common:save")
    public R save(FileDO sysFile)
    {
        if (sysFileService.save(sysFile) > 0)
        {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("common:update")
    public R update(@RequestBody FileDO sysFile)
    {
        sysFileService.update(sysFile);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    // @RequiresPermissions("common:remove")
    public R remove(Long id, HttpServletRequest request)
    {
        String fileName = rtmdoConfig.getUploadPath()
                + sysFileService.get(id).getUrl().replace("/files/", "");
        if (sysFileService.remove(id) > 0)
        {
            boolean b = FileUtils.deleteFile(fileName);
            if (!b)
            {
                return R.error("数据库记录删除成功，文件删除失败");
            }
            return R.ok();
        }
        else
        {
            return R.error();
        }
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:remove")
    public R remove(@RequestParam("ids[]") Long[] ids)
    {
        sysFileService.batchRemove(ids);
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/upload")
    R upload(@RequestParam("file") MultipartFile file,
            HttpServletRequest request)
    {
        String fileName = file.getOriginalFilename();
        fileName = FileUtils.renameToUUID(fileName);
        FileDO sysFile = new FileDO(FileType.fileType(fileName),
                "/files/" + fileName, file.getOriginalFilename(), new Date());
        try
        {
            FileUtils.uploadFile(file.getBytes(), rtmdoConfig.getUploadPath(),
                    fileName);
        }
        catch (Exception e)
        {
            return R.error();
        }

        if (sysFileService.save(sysFile) > 0)
        {
            return R.ok().put("fileName", file.getOriginalFilename()).put("url",
                    sysFile.getUrl());
        }
        return R.error();
    }

    @GetMapping("/attachment")
    String attachment()
    {
        return "utils/uploader";
    }
}
