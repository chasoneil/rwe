package com.chason.rwe.controller.finder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 找到设备
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */
public class DeviceFinderThread extends Thread
{
    private String _ip;
    public void setIp(String ip)
    {
        this._ip = ip;
    }

    public void run()
    {
        try
        {
            int intRandom = (int)(9000 + Math.random()*(9999 - 9000 + 1));
            DatagramSocket socket = new DatagramSocket(intRandom);
            String text = "HLK";

            byte[] buf = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(this._ip), 988);
            socket.send(packet);

            displayReciveInfo(socket);
            socket.close();
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 接收数据并打印出来
     * @param socket
     * @throws IOException
     */
    private void displayReciveInfo(DatagramSocket socket)
            throws IOException
    {
        do
        {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            System.out.println("收到数据:" + packet.getAddress());
            InetAddress address = packet.getAddress();
            IpInfoInstance.INSTANCE.add(address.toString().replace("/", ""));
        }
        while(true);
    }
}
