package com.chason.common.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.chason.system.domain.UserDO;
import com.chason.system.service.SessionService;

@Component
public class MsgNotifyService
{
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SessionService sessionService;

    public void sendMsgToOnlineUsers(String msg)
    {
        // 给在线用户发送通知
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                for (UserDO userDO : sessionService.listOnlineUser())
                {
                    template.convertAndSendToUser(userDO.toString(), "/queue/notifications", msg);

                }
            }
        });
        executor.shutdown();
    }

    /**
     * 给所有在线的指定用户发送信息
     * @param userIds 指定用户id数组
     * @param msg 需要发送的信息
     * */
    public void sendMsgToReceiver(Long[] userIds, String msg)
    {
        // 给在线用户发送通知
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                for (UserDO userDO : sessionService.listOnlineUser())
                {
                    for (Long userId : userIds)
                    {
                        if (userId.equals(userDO.getUserId()))
                        {
                            template.convertAndSendToUser(userDO.toString(), "/queue/notifications", msg);
                        }
                    }
                }
            }
        });
        executor.shutdown();


    }

}
