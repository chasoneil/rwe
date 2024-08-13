package com.chason.rwe.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.chason.rwe.socket.SwitchDeviceReceive;

/**
 * 添加对socket的侦听
 **/
public class PlugSocketListenServer implements ApplicationListener<ContextRefreshedEvent>
{

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (event.getApplicationContext().getParent() == null) {
            // 启动线程，防止阻塞主线程
//            new Thread()
//            {
//                public void run()
//                {
//                    //启动端口侦听
//                    System.out.println("ヾ启动端口侦听ヾ \n");
//                    SwitchDeviceReceive theListen = new SwitchDeviceReceive();
//                    theListen.run();
//                }
//            }.start();
        }
    }
}
