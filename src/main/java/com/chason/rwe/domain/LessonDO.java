package com.chason.rwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LessonDO {

    private String lessonId;

    private String lesson;

    private int count;

    private int learned;

    private int learnedTime;

    private int passed;

    private Date passTime;

    private Date lastLearnTime;

}
