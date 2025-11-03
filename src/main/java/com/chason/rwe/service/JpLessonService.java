package com.chason.rwe.service;

import com.chason.rwe.domain.JpLessonDO;

import java.util.List;
import java.util.Map;

public interface JpLessonService {

    JpLessonDO get(Integer id);

    List<JpLessonDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(JpLessonDO lesson);

    int update(JpLessonDO lesson);

    int remove(Integer id);

    int delete(Integer lesson);

    int batchRemove(Integer[] ids);

}
