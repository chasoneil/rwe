package com.chason.rwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class JpLessonDO {

    private Integer id;

    /*
     课程所属用户id, 公共课程的userId为1
     公共课程只有管理员能创建
     普通用户创建的都是私有课程
     */
    private Integer userId;

    // 课程名称
    private String lesson;

    // 词数
    private int count;

    private int passed;

    // 本课的上次学习时间
    private Date lastLearnTime;

}
