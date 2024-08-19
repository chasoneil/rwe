package com.chason.rwe.dao;

import com.chason.rwe.domain.LessonDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LessonDao {

    LessonDO get(String lessonId);

    List<LessonDO> list(Map<String,Object> map);

    LessonDO findByName(String lesson);

    int count(Map<String,Object> map);

    int save(LessonDO lesson);

    int update(LessonDO lesson);

    int remove(String lessonId);

    int batchRemove(String[] lessonIds);

}
