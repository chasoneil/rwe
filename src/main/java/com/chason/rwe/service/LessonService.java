package com.chason.rwe.service;

import com.chason.rwe.domain.LessonDO;

import java.util.List;
import java.util.Map;

public interface LessonService {

    LessonDO get(String lessonId);

    List<LessonDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(LessonDO lesson);

    int update(LessonDO lesson);

    int remove(String lessonId);

    int delete(String lesson);

    int batchRemove(String[] lessonIds);

}
