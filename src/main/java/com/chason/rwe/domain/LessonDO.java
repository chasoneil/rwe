package com.chason.rwe.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LessonDO {

    private String lessonId;

    /**
     * lesson name
     */
    private String lesson;

    /**
     * words count
     */
    private int count;

    /**
     * the count of learned words
     */
    private int learned;

    // learn times
    private int learnedTime;

    private int passed;

    private Date passTime;

    private Date lastLearnTime;

}
