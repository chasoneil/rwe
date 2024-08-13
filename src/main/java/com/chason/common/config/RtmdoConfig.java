package com.chason.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rtmdo")
public class RtmdoConfig
{
    // 上传路径
    private String uploadPath;
    public String getUploadPath()
    {
        return uploadPath;
    }
    public void setUploadPath(String uploadPath)
    {
        this.uploadPath = uploadPath;
    }

    //数据备份路径
    private String dbRepoPath;
    public String getDbRepoPath()
    {
        return this.dbRepoPath;
    }
    public void setDbRepoPath(String dbRepoPath)
    {
        this.dbRepoPath = dbRepoPath;
    }
}
