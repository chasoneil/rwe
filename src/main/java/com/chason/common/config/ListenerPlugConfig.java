package com.chason.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chason.rtm.listener.PlugSocketListenServer;

/**
 * 启动各类侦听服务
 * */
@Configuration
public class ListenerPlugConfig
{
    @Bean
    public PlugSocketListenServer sysSourceStartListener()
    {
        return new PlugSocketListenServer();
    }
}
