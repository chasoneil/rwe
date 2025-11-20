package com.chason.common.task;

import com.chason.common.annotation.Log;
import com.chason.common.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * 定期清理数据库的操作审计日志
 * @author chason
 * */
@Component
@Slf4j
public class JobCleanLog implements Job {
    @Autowired
    private LogService logService;

    @Log("清理操作日志任务")
    @Override
    public void execute(JobExecutionContext arg0)
            throws JobExecutionException {

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            // 保留7天的数据
            cal.add(Calendar.DATE, -7);
            Date lastTime = cal.getTime();
            int count = logService.removeBeforeDate(lastTime);
            log.info("JobCleanLog remove log, count:{}", count);
        } catch (Exception e) {
            log.error("JobCleanLog caught error:{}", e.getMessage());
        }
    }
}
