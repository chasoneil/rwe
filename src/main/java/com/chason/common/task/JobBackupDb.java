package com.chason.common.task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chason.common.annotation.Log;
import com.chason.common.config.RtmdoConfig;
import com.chason.common.domain.LogDO;
import com.chason.common.service.LogService;
import com.chason.common.utils.CompressUtils;
import com.chason.rwe.value.RandomFlagValue;

/**
 * 每日自动备份数据库
 * 自动检查并删除保存一个月时间以上的log
 * */
@Component
public class JobBackupDb implements Job
{
//    @Value("${rtmdo.dbRepoPath}")
//    String _dbRepoPath;

    @Autowired
    private RtmdoConfig _rtmdoConfig;

    @Autowired
    private LogService logService;

    @Log("数据库备份任务")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
        //清除今日随机策略
        RandomFlagValue value = RandomFlagValue.getInstance();
        value.getActiveFlag().clear();

        String fPath    = this._rtmdoConfig.getDbRepoPath() + "/zmanager.sql";
        String destPath = this._rtmdoConfig.getDbRepoPath() + "/zmanager_" + new Date().getTime()+".zip";
        deleteLog();
        dumpFile(fPath);
        CompressUtils.zip(fPath, destPath, false, "zmanager");
    }

    /**
     * 定时删除一个月之前的Log数据
     */
    private void deleteLog()
    {
        Map<String, Object> param = new HashMap<>();
        List<LogDO> allLists = logService.list(param);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        Date lastTime = cal.getTime();

        for (LogDO theLog : allLists)
        {
            if(theLog.getGmtCreate().before(lastTime))
            {
                logService.remove(theLog.getId());
            }
        }
    }

    /**
     * 生成数据库备份文件
     * */
    private void dumpFile(String fPath)
    {
        String strCommand = "mysqldump -hlocalhost -uroot -p54363751 --default-character-set=utf8 zmanager";

        Runtime rt = Runtime.getRuntime();
        try
        {
            Process child = rt.exec(strCommand);
            InputStream in = child.getInputStream();
            InputStreamReader input = new InputStreamReader(in, "utf8");

            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;

            BufferedReader br = new BufferedReader(input);
            while ((inStr = br.readLine()) != null)
            {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            FileOutputStream fout = new FileOutputStream(fPath);
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
            writer.write(outStr);
            writer.flush();

            in.close();
            input.close();
            br.close();
            writer.close();
            fout.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
