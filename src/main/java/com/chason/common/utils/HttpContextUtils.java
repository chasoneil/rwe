package com.chason.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContextUtils
{
    public static HttpServletRequest getHttpServletRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
    }
    public static HttpServletResponse getHttpServletResponse()
    {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getResponse();
    }
}
