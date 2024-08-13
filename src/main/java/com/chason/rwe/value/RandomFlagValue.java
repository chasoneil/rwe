package com.chason.rwe.value;

import java.util.HashSet;

/**
 * 常驻内存，随机策略是否执行标志
 * @author chason
 */
public class RandomFlagValue
{
    private HashSet<String> activeFlag = new HashSet<String>();
    public HashSet<String> getActiveFlag()
    {
        return activeFlag;
    }
    private static RandomFlagValue instance = new RandomFlagValue();
    private RandomFlagValue(){}
    public static RandomFlagValue getInstance() {
        return instance;
    }
}
