package com.chason.common.task;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chason.common.annotation.Log;
import com.chason.common.config.RweConfig;

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
    private RweConfig _rtmdoConfig;

    @Log("数据库备份任务")
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //清除今日随机策略
        RandomFlagValue value = RandomFlagValue.getInstance();
        value.getActiveFlag().clear();

        String fPath    = this._rtmdoConfig.getDbRepoPath() + "/rwe.sql";
        String destPath = this._rtmdoConfig.getDbRepoPath() + "/rwe_" + new Date().getTime()+".zip";
        dumpFile(fPath);
        CompressUtils.zip(fPath, destPath, false, "rwe");
    }

    /**
     * 生成数据库备份文件
     * */
    private void dumpFile(String fPath) {
        String strCommand = "mysqldump -hlocalhost -uroot -p54363751 --default-character-set=utf8 rwe";

        Runtime rt = Runtime.getRuntime();
        try {
            Process child = rt.exec(strCommand);
            InputStream in = child.getInputStream();
            InputStreamReader input = new InputStreamReader(in, StandardCharsets.UTF_8);

            String inStr;
            StringBuilder sb = new StringBuilder();
            String outStr;

            BufferedReader br = new BufferedReader(input);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            FileOutputStream fout = new FileOutputStream(fPath);
            OutputStreamWriter writer = new OutputStreamWriter(fout, StandardCharsets.UTF_8);
            writer.write(outStr);
            writer.flush();

            in.close();
            input.close();
            br.close();
            writer.close();
            fout.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
