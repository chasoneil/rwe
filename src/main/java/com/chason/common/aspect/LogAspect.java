package com.chason.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chason.common.annotation.Log;
import com.chason.common.dao.LogDao;
import com.chason.common.domain.LogDO;
import com.chason.common.utils.HttpContextUtils;
import com.chason.common.utils.IPUtils;
import com.chason.common.utils.JSONUtils;
import com.chason.common.utils.ShiroUtils;
import com.chason.system.domain.UserDO;

@Aspect
@Component
@Slf4j
public class LogAspect {

	@Autowired
	LogDao logMapper;

	@Pointcut("@annotation(com.chason.common.annotation.Log)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		//异步保存日志
		saveLog(point, time);
		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		LogDO sysLog = new LogDO();
		Log syslog = method.getAnnotation(Log.class);
		if (syslog != null) {
			// 注解上的描述
			sysLog.setOperation(syslog.value());
		}
		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");
		// 请求的参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = JSONUtils.beanToJson(args[0]).substring(0, 4999);
			sysLog.setParams(params);
		} catch (Exception e) {
            log.warn("save log error {}", e.getMessage());
		}

		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		sysLog.setIp(IPUtils.getIpAddr(request));
		UserDO currUser = ShiroUtils.getUser();

		if (null == currUser) {
			if (null != sysLog.getParams()) {
				sysLog.setUserId(-1L);
				sysLog.setUsername(sysLog.getParams());
			} else {
				sysLog.setUserId(-1L);
				sysLog.setUsername("获取用户信息为空");
			}
		} else {
			sysLog.setUserId(ShiroUtils.getUserId());
			sysLog.setUsername(ShiroUtils.getUser().getUsername());
		}
		sysLog.setTime((int) time);
		sysLog.setGmtCreate(new Date());
		logMapper.save(sysLog);
	}
}
