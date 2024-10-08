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
public class RweApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RweApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ   背单词启动成功     ヾ(◍°∇°◍)ﾉﾞ\n");
    }
}
