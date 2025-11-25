package com.chason.common.listenner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.chason.common.quartz.utils.QuartzManager;
import com.chason.common.service.JobService;

@Component
@Order(value = 1)
@Slf4j
public class ScheduleJobInitListener implements CommandLineRunner {

	@Autowired
	JobService scheduleJobService;

	@Autowired
	QuartzManager quartzManager;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			scheduleJobService.initSchedule();
		} catch (Exception e) {
			log.warn("schedule job caught exception:{}", e.getMessage());
		}

	}
}
