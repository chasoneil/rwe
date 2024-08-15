package com.chason.rwe.service;

import com.chason.rwe.domain.LessonDO;

import java.util.List;
import java.util.Map;

public interface LessonService {

    LessonDO get(int lessonId);

    List<LessonDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(LessonDO lesson);

    int update(LessonDO lesson);

    int remove(int lessonId);

    int batchRemove(int[] lessonIds);

}
