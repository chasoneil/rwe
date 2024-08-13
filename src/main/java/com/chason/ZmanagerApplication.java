package com.chason;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.chason.*.dao")
@SpringBootApplication
public class ZmanagerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ZmanagerApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ   小乙网控管家启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
    }
}
