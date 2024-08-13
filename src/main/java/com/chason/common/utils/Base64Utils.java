package com.chason.common.utils;

import org.apache.commons.codec.binary.Base64;
public class Base64Utils
{
    /**
     * base64加密
     * 应用场景：email、密钥、证书文件。
     * */
    public static String BASE64Encoder(String src)
    {
        byte[] encode = Base64.encodeBase64(src.getBytes());
        return new String(encode);
    }

    /**
     * base64解密
     * base64加密的应用场景：email、密钥、证书文件。
     * */
    public static String BASE64Decoder(String src)
    {
        byte[] decode = Base64.decodeBase64(src.getBytes());
        return new String(decode);
    }

}
