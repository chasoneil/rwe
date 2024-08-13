package com.chason.rwe.socket;

import java.io.StringReader;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 侦听信息的单例类
 */
public class SwitchDeviceMap
{
    private SwitchDeviceMap()
    {
    }

    private static class SingletonHolder
    {
        private final static SwitchDeviceMap instance = new SwitchDeviceMap();
    }

    public static SwitchDeviceMap getInstance()
    {
        return SingletonHolder.instance;
    }

    /**
     * ##########################################
     * */
    Map<String, SwitchDeviceValue> _mSwitchDeviceValues = new HashMap<String, SwitchDeviceValue>();
    /**
     * 得到SwitchDeviceValue的map数据
     * key:macAddress
     * */
    public Map<String, SwitchDeviceValue> getSwitchDeviceValueMap()
    {
        return this._mSwitchDeviceValues;
    }

    /**
     * 取出SwitchDeviceValue
     * @param macAddress mac地址
     * */
    public SwitchDeviceValue getSwitchDeviceValue(String macAddress)
    {
        return this._mSwitchDeviceValues.get(macAddress);
    }

    /**
     * ##########################################
     * */
    Map<String, String> _mCommands = new HashMap<String, String>();
    /**
     * 得到SwitchDeviceValue的map数据
     * key:macAddress
     * */
    public Map<String, String> getCommandMap()
    {
        return this._mCommands;
    }

    /**
     * 保存SwitchDeviceValue的数组
     * @param macAddress mac地址
     * @param value 获得的值
     * */
    public void putCommandMap(String macAddress, String command)
    {
        this._mCommands.put(macAddress, command);
    }

    /**
     * ##########################################
     * */
    Map<String, StringBuffer> _mStringBuffers = new HashMap<String, StringBuffer>();
    /**
     * 从侦听口获取的数据根据ip地址保存入对应的StringBuffer。
     * @param serverSocket 服务器的socket
     * @param ipAddress ip地址
     * @param data 获取的数据
     * */
    public SwitchDeviceValue getStringBufferMap(ServerSocket serverSocket, String ipAddress, String data)
    {
        StringBuffer sBuffer = this._mStringBuffers.get(ipAddress);
        if (sBuffer == null)
        {
            sBuffer = new StringBuffer("");
            this._mStringBuffers.put(ipAddress, sBuffer);
        }
        sBuffer.append(data);
        String strXml = getXml(sBuffer);

        if (strXml.equals("ERROR"))
        {
            //清除多余的头部数据
            this._mStringBuffers.put(ipAddress, new StringBuffer(getLastXml(sBuffer)));
            return null;
        }

        if (strXml.equals(""))
        {
            return null;
        }

        try
        {
            //如果获得了完整的xml数据，则需要把获得的数据从sBuffer中清除。
            this._mStringBuffers.put(ipAddress, new StringBuffer(getLastXml(sBuffer)));

            //解析xml数据，并完成赋值
            SwitchDeviceValue theValue = new SwitchDeviceValue();
            theValue.setServerSocket(serverSocket);
            theValue.setIpAddress(ipAddress);

            StringReader sr = new StringReader(strXml);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=docFactory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList nl2 = doc.getElementsByTagName("Umac");
            if(nl2 != null)
            {
                Node node2 = nl2.item(0).getFirstChild();
                String mac = node2.getNodeValue();
                theValue.setMacAddress(mac);
            }

            NodeList nl3 = doc.getElementsByTagName("Utime");
            if(nl3 != null)
            {
                //Node node3 = nl3.item(0).getFirstChild();
                //theValue.setUpdateTime(node3.getNodeValue());
            }

            NodeList nl4 = doc.getElementsByTagName("Format");
            if(nl4 != null)
            {
                Node node4 = nl4.item(0).getFirstChild();
                theValue.setFormat(node4.getNodeValue());
            }

            NodeList nl5 = doc.getElementsByTagName("Switch");
            if(nl5 != null)
            {
                Node node5 = nl5.item(0).getFirstChild();
                theValue.setSwitch(node5.getNodeValue());
            }

            NodeList nl6 = doc.getElementsByTagName("Current");
            if(nl6 != null)
            {
                Node node6 = nl6.item(0).getFirstChild();
                theValue.setCurrent(node6.getNodeValue());
            }

            NodeList nl7 = doc.getElementsByTagName("Manual");
            if(nl7 != null)
            {
                Node node7 = nl7.item(0).getFirstChild();
                theValue.setManual(node7.getNodeValue());
            }

            //System.out.println("***********收到 " + theValue.getIpAddress() + ":" + " 发来的消息!switch:" + theValue.getSwitch() + "/current:" + theValue.getCurrent() + "/manual:" + theValue.getManual());
            SwitchDeviceMap theMap = SwitchDeviceMap.getInstance();
            SwitchDeviceValue theLastValue = theMap.getSwitchDeviceValue(theValue.getMacAddress()); //得到原来的信息
            if (theLastValue != null)
            {
                theValue.setSwitchLast(theLastValue.getSwitch());
            }

            this._mSwitchDeviceValues.put(theValue.getMacAddress(), theValue);

            return theValue;
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            System.out.println("解析xml失败！ ：" + strXml);
        }//end try

        return null;
    }

    private static String getXml(StringBuffer sBuffer)
    {
        String strTmp = sBuffer.toString();
        String strResult = "";
        if(strTmp.indexOf("<?xml") > -1 && strTmp.indexOf("</Request>") > -1)
        {
            int intStart = strTmp.indexOf("<?xml");
            int intFinish = strTmp.indexOf("</Request>") + 10;
            if (intFinish > intStart)
            {
                strResult = strTmp.substring(intStart, intFinish);
            }
            else
            {
                return "ERROR";
            }
        }

        return strResult;
    }

    /**
     * 获取传输数据中</Request>内容截止后的数据
     * */
    private static String getLastXml(StringBuffer sBuffer)
    {
        String strTmp = sBuffer.toString();
        int intIndex = strTmp.lastIndexOf("</Request>");
        return strTmp.substring(intIndex + 10, strTmp.length());
    }
}
