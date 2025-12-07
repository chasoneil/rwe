package com.chason.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chason.common.domain.TaskDO;
import com.chason.common.service.JobService;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common/job")
@Slf4j
public class JobController extends BaseController{

	private static final String PREFIX = "common/job";

	@Autowired
	private JobService taskScheduleJobService;

	@GetMapping()
	String taskScheduleJob() {
		return PREFIX + "/job";
	}

	@ResponseBody
	@GetMapping("/list")
    PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<TaskDO> taskScheduleJobList = taskScheduleJobService.list(query);
		int total = taskScheduleJobService.count(query);
		PageUtils pageUtils = new PageUtils(taskScheduleJobList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	String add() {
		return PREFIX + "/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		TaskDO job = taskScheduleJobService.get(id);
		model.addAttribute("job", job);
		return PREFIX + "/edit";
	}

	@RequestMapping("/info/{id}")
    R info(@PathVariable("id") Long id) {
		TaskDO taskScheduleJob = taskScheduleJobService.get(id);
		return R.ok().put("taskScheduleJob", taskScheduleJob);
	}

	@ResponseBody
	@PostMapping("/save")
    R save(TaskDO taskScheduleJob) {
		if (taskScheduleJobService.save(taskScheduleJob) > 0) {
			return R.ok();
		}
		return R.error();
	}

	@ResponseBody
	@PostMapping("/update")
    R update(TaskDO taskScheduleJob) {
		taskScheduleJobService.update(taskScheduleJob);
		return R.ok();
	}

    @ResponseBody
    @PostMapping("/do/job")
    R doJob (Long id) {
        try {
            taskScheduleJobService.startJobNow(id);
        } catch (Exception e) {
            return R.error();
        }
        return R.ok();
    }

    @ResponseBody
	@PostMapping("/remove")
    R remove(Long id) {
		if (taskScheduleJobService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

    @ResponseBody
	@PostMapping("/batchRemove")
    R remove(@RequestParam("ids[]") Long[] ids) {
		taskScheduleJobService.batchRemove(ids);
		return R.ok();
	}

    @ResponseBody
	@PostMapping(value = "/changeJobStatus")
    R changeJobStatus(Long id,String cmd ) {
		String label = "";
		if ("start".equals(cmd)) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			taskScheduleJobService.changeStatus(id, cmd);
			return R.ok("任务" + label + "成功");
		} catch (Exception e) {
            log.warn("change job status error: jobId {}, cmd {}", id, cmd);
            return R.error();
		}
	}
}
