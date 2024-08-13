package com.chason.common.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 消息摘要算法，属于不可逆算法
 * 应用场景：密码加密，文件摘要验证
 * */
public class MD5Utils
{
    private static final String SALT            = "1qazxsw2";

    private static final String ALGORITH_NAME   = "md5";

    private static final int    HASH_ITERATIONS = 2;

    /**
     * 原密码加密生成新密码
     * */
    public static String encrypt(String pswd)
    {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    /**
     * 用户名和原密码加密，生成新密码
     * */
    public static String encrypt(String username, String pswd)
    {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT), HASH_ITERATIONS).toHex();
        return newPassword;
    }

    /**
     * 签名生成算法
     * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
     * @param String secret 签名密钥
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(Map<String, String> params,
            String secret) throws IOException
    {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Entry<String, String> param : entrys)
        {
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        basestring.append(secret);

        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        }
        catch (GeneralSecurityException ex)
        {
            throw new IOException(ex);
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1)
            {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }

}
