package com.chason.rwe.socket;

import java.net.ServerSocket;

/**
 *
 * */
public class SwitchDeviceValue
{
    private String _ipAddress;
    private String _macAddress;
    private java.util.Date _updateTime;
    private String _format;
    private String _switch; // 设备通电情况 10表示设备通电中， 00表示设备未通电
    private String _switchLast; // 上次设备通电情况 10表示设备通电中， 00表示设备未通电
    private String _current; //设备的负载情况， 10 表示有负载， 00 表示没负载
    private String _manual;
    private String _rs485;

    public String getIpAddress()
    {
        return _ipAddress;
    }
    public void setIpAddress(String ipAddress)
    {
        this._ipAddress = ipAddress;
    }
    public String getMacAddress()
    {
        return _macAddress;
    }
    public void setMacAddress(String macAddress)
    {
        this._macAddress = macAddress;
    }
    public java.util.Date getUpdateTime()
    {
        return _updateTime;
    }
    public void setUpdateTime(java.util.Date updateTime)
    {
        this._updateTime = updateTime;
    }
    public String getFormat()
    {
        return _format;
    }
    public void setFormat(String format)
    {
        this._format = format;
    }
    public String getSwitch()
    {
        return _switch;
    }
    public void setSwitch(String switchF)
    {
        this._switch = switchF;
    }
    public String getSwitchLast()
    {
        return _switchLast;
    }
    public void setSwitchLast(String switchFLast)
    {
        this._switchLast = switchFLast;
    }
    public String getCurrent()
    {
        return _current;
    }
    public void setCurrent(String current)
    {
        this._current = current;
    }
    public String getManual()
    {
        return _manual;
    }
    public void setManual(String manual)
    {
        this._manual = manual;
    }
    public String getRs485()
    {
        return _rs485;
    }
    public void setRs485(String rs485)
    {
        this._rs485 = rs485;
    }

    private ServerSocket _server;
    /**
     * 建立的ServerSocket
     * */
    public ServerSocket getServerSocket()
    {
        return this._server;
    }
    public void setServerSocket(ServerSocket server)
    {
        this._server = server;
    }
}
