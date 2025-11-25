package com.chason.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 过滤器,表单提交的date数据都会通过过滤器进行过滤
 */
@Configuration
@Slf4j
public class DateConvertConfig {
    @Bean
    public Converter<String, Date> stringDateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(source);
                } catch (Exception e) {
                    SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = sdfday.parse(source);
                    } catch (ParseException e1) {
                        log.warn("parse date exception:{}", e.getMessage());
                    }
                }
                return date;
            }
        };
    }
}
