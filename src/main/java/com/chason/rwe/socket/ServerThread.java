package com.chason.rwe.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.chason.common.config.ApplicationContextRegister;
import com.chason.common.utils.GUIDUtils;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.service.DeviceService;
import com.chason.rwe.utils.PolicyTimeUtils;

public class ServerThread extends Thread
{
    // 和本线程相关的Socket
    ServerSocket server = null;
    Socket socket = null;

    public ServerThread(ServerSocket server, Socket socket)
    {
        this.server = server;
        this.socket = socket;
    }

    // 线程执行的操作，响应客户端的请求
    public void run()
    {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        try
        {
            String ip = socket.getInetAddress().getHostAddress();

            // 获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String data = null;
            while ((data = br.readLine()) != null)
            {
                // 循环读取客户端的信息
                data = data.replace(System.getProperty("line.separator"), "");//System.getProperty("line.separator") 在Windows平台上，行分隔符字符串是\r\n，在Unix平台上是\n。
                //System.err.println("###" + ip + ":" + data);

                //数据传递至处理对象
                SwitchDeviceMap theMap = SwitchDeviceMap.getInstance();
                SwitchDeviceValue theValue = theMap.getStringBufferMap(this.server, ip, data);

                /**
                 * 根据mac地址获取设备信息，如果没有则创建一条
                 * 如果是已经通过审核的设备，则更新设备心跳时间
                 * */
                if (theValue != null)
                {
                    //设备的devNumber -> mac
                    //System.out.println("###marc:" + theValue.getMacAddress());
                    DeviceService deviceService = ApplicationContextRegister.getBean(DeviceService.class);
                    DeviceDO theDevice = deviceService.findByNumber(theValue.getMacAddress());
                    if (theDevice == null)
                    {
                        if (theValue.getMacAddress().length() <= 20)
                        {
                            theDevice = new DeviceDO();
                            theDevice.setDevId          (GUIDUtils.generateGUID(theDevice));
                            theDevice.setDevCode        (ip);
                            theDevice.setDevNumber      (theValue.getMacAddress());
                            theDevice.setDevRegBy       ("自动获取");
                            theDevice.setDevRegTime     (new java.util.Date());
                            theDevice.setDevType        ("ZTOWER");
                            theDevice.setDevStatus      ("00");
                            theDevice.setDevSwitchMode  ("自动");
                            theDevice.setDevPolicy      ("-");
                            deviceService.save(theDevice);
                        }//end if
                    }
                    else
                    {
                        if (!ip.equals(theDevice.getDevCode()))
                        {
                            theDevice.setDevCode(ip);
                        }
                        theDevice.setDevStatus("11");
                        theDevice.setDevOnlineLastTime(new java.util.Date());

                        if ("自动".equals(theDevice.getDevSwitchMode()) && "00".equals(theValue.getSwitchLast()) && "10".equals(theValue.getSwitch())) //如果状态发生变化，并且变化的时间不再策略期内
                        {
                            if (StringUtils.isNotNull(theDevice.getDevPolicy()) && !"-".equals(theDevice.getDevPolicy()))
                            {
                                List<String> arrPolicy = JSON.parseArray(theDevice.getDevPolicy(), String.class);
                                if (!PolicyTimeUtils.isPolicyOpen(arrPolicy))
                                {
                                    theDevice.setDevSwitchMode("手动");
                                    theDevice.setDevSwitchHandTime(new java.util.Date());
                                }//end if
                            }//end if
                            else
                            {
                                theDevice.setDevSwitchMode("手动");
                                theDevice.setDevSwitchHandTime(new java.util.Date());
                            }
                        }//endif
                        deviceService.update(theDevice);
                    }//end if

                    //执行可能的命令
                    Map<String, String> mCommands = theMap.getCommandMap();
                    String strCommandInfo = mCommands.get(theDevice.getDevNumber());
                    //System.err.println("@@@@@command:" + strCommandInfo);
                    if (StringUtils.isNotNull(strCommandInfo))
                    {
                        //执行单个命令
                        if(strCommandInfo.indexOf("[") == -1)
                        {
                            String[] strTmp = strCommandInfo.split(":");
                            if (strTmp.length == 1)
                            {
                                SwitchDeviceSendUtils.command(socket, strTmp[0], null);
                            }
                            else
                            {
                                SwitchDeviceSendUtils.command(socket, strTmp[0], strTmp[1]);
                            }
                            mCommands.put(theDevice.getDevNumber(), null);
                        }
                        else //执行命令合集
                        {
                            List<String> commands = JSON.parseArray(strCommandInfo, String.class);

                            for (String command : commands)
                            {
                                System.err.println("command:" + command);
                                String[] strTmp = command.split(":");
                                if (strTmp.length == 1)
                                {
                                    SwitchDeviceSendUtils.command(socket, strTmp[0], null);
                                }
                                else
                                {
                                    SwitchDeviceSendUtils.command(socket, strTmp[0], strTmp[1]);
                                }
                                mCommands.put(theDevice.getDevNumber(), null);
                            }
                        }
                    }

                }//end if
            }

            socket.shutdownInput();// 关闭输入流
            // 获取输出流，响应客户端的请求
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write("关闭socket！");
            pw.flush();// 调用flush()方法将缓冲输出
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            // 关闭资源
            try
            {
                if (pw != null)
                    pw.close();
                if (os != null)
                    os.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
