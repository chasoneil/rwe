package com.chason.rwe.socket;
import java.net.ServerSocket;
import java.net.Socket;

public class SwitchDeviceReceive
{
    /**
     * 接收终端报送的状态数据
     */
    public void run()
    {
        int intSocketPort = 8280;

        //启动模拟器执行命令操作
        System.out.println("### TcpReceiver started on " + intSocketPort + "!");
        try
        {
            ServerSocket server = new ServerSocket(intSocketPort);

            while(true)
            {
                //接收数据
                Socket socket = server.accept();

                ServerThread serverThread = new ServerThread(server, socket);
                serverThread.start();

            }//end while
        }
        catch(Exception se)
        {
            se.printStackTrace();
            return;
        }//end try

    }

    public static String getHostIp()
    {
        java.net.InetAddress localInetAddress = null;
        try
        {
            // get the inet address
            localInetAddress = java.net.InetAddress.getLocalHost();
        }
        catch (java.net.UnknownHostException uhe)
        {
            uhe.printStackTrace();
            return null;
        }
        return localInetAddress.getHostAddress();
    }
}
