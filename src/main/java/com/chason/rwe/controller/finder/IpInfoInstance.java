package com.chason.rwe.controller.finder;

import java.util.Map;
import java.util.TreeMap;

public enum IpInfoInstance
{
    INSTANCE;
    private Map<String, String> mIPs = new TreeMap<>();
    public void add(String ip)
    {
        mIPs.put(ip, "-");
    }

    public Map<String, String> getAll()
    {
        return mIPs;
    }

    public void cleanAll()
    {
        mIPs = new TreeMap<>();
    }
}
