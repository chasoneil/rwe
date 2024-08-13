package com.chason.rwe.socket;

import java.net.ServerSocket;
import java.net.Socket;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SwitchDeviceSendUtils.class)
public class TestSwitchDeviceSendUtils
{
    private static ServerSocket _server = null;
    private static Socket _socket = null;

    /**
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _server = new ServerSocket(8280);
        _socket = _server.accept();
        String ip = _socket.getInetAddress().getHostAddress();
        System.out.println(ip + "....connected");
    }

    /**
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        _socket.close();
        _server.close();

    }

    @Test
    public void testREBOOT()
    {
        Assert.assertTrue(SwitchDeviceSendUtils.command(_socket, "REBOOT", null));
    }

    @Test
    public void testSWITCHOPEN()
    {
        Assert.assertTrue(SwitchDeviceSendUtils.command(_socket, "SWITCHOPEN", null));
    }

    @Test
    public void testSWITCHCLOSE()
    {
        Assert.assertTrue(SwitchDeviceSendUtils.command(_socket, "SWITCHCLOSE", null));
    }

}
