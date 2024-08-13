package com.chason.rwe.socket;
import java.io.DataOutputStream;
import java.net.Socket;

import com.chason.common.utils.MathUtils;
import com.chason.common.utils.StringUtils;

public class SwitchDeviceSendUtils
{
    /**
     * 处理命令
     * @param socket 接收数据的socket
     * @param commandType 执行命令类型
     * @param command 需要执行的内容
     * */
    public static boolean command(Socket socket, String commandType, String command)
    {
        System.err.println("***command***" + commandType);
        if ("REBOOT".equals(commandType))
        {
            return reboot(socket);
        }
        else if ("SWITCHOPEN".equals(commandType))
        {
            return switchOpen(socket);
        }
        else if ("SWITCHCLOSE".equals(commandType))
        {
            return switchClose(socket);
        }
        else if ("TIMINGOPEN".equals(commandType))
        {
            return timingOpen(socket);
        }
        else if ("TIMINGOPENTIME".equals(commandType))
        {
            if (StringUtils.isNotNull(command))
            {
                return timingOpenTime(socket, command);
            }
        }
        else if ("TIMINGCLOSE".equals(commandType))
        {
            return timingClose(socket);
        }
        else if ("TIMINGCLOSETIME".equals(commandType))
        {
            if (StringUtils.isNotNull(command))
            {
                return timingCloseTime(socket, command);
            }
        }
        else if ("DELAYEDOPEN".equals(commandType))
        {
            return delayedOpen(socket);
        }
        else if ("DELAYEDCLOSE".equals(commandType))
        {
            return delayedClose(socket);
        }
        else if ("DELAYEDOPENTIME".equals(commandType))
        {
            if (StringUtils.isNotNull(command))
            {
                return delayedOpenTime(socket, command);
            }
        }
        else if ("SYNCCLOCKTIME".equals(commandType))
        {
            if (StringUtils.isNotNull(command))
            {
                return syncClockTime(socket, command);
            }
        }
        return false;
    }

    /**
     * 重启设备
     * */
    private static boolean reboot(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE030200FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行重启设备");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 打开电源
     * */
    private static boolean switchOpen(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04010100FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行打开电源");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 关闭电源
     * */
    private static boolean switchClose(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04000100FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行关闭电源");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 定时功能开
     * */
    private static boolean timingOpen(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04010300FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行定时功能开");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 定时功能开-设置时间
     * */
    private static boolean timingOpenTime(Socket socket, String time)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE05"+ strToHex(time) +"0400FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行设置定时开启时间" + time);
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 定时功能关
     * */
    private static boolean timingClose(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04000300FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行定时功能关");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 定时功能开-设置时间
     * */
    private static boolean timingCloseTime(Socket socket, String time)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE05"+ strToHex(time) +"0500FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行定时关闭时间" + time);
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 延时功能-有效
     * */
    private static boolean delayedOpen(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04010600FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行延时功能开:" + reply);
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 延时功能-无效
     * */
    private static boolean delayedClose(Socket socket)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04000600FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行延时功能关");
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 延时功能-时长设置
     * */
    private static boolean delayedOpenTime(Socket socket, String time)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String reply = "FE04"+ strToHex(time) +"0700FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行设置延时时长" + reply);
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 同步主机时间
     * */
    private static boolean syncClockTime(Socket socket, String time)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("##### 执行同步主机时间1:" + time);
            String reply = "FE09"+ strToHex(time) +"0800FE";
            //String reply = "FE09"+ time +"0800FE";
            out.write(StringUtils.hexStrToBinaryStr(reply));
            out.flush();
            System.out.println("##### 执行同步主机时间:" + reply);
            //socket.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 10进制命令改16进制字符串
     * */
    private static String strToHex(String strTime)
    {
        String[] arrTime = StringUtils.stringSpilt(strTime, 2);
        String strResult = "";
        for (String str : arrTime)
        {
            str = MathUtils.intToHex(Integer.parseInt(str));
            strResult += StringUtils.formatByLength_LeftFill(str, 2, "0");
        }

        return strResult;
    }
}
