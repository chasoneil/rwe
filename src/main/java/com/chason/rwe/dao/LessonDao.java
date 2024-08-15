package com.chason.rwe.dao;

import com.chason.rwe.domain.LessonDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LessonDao {

    LessonDO get(int lessonId);

    List<LessonDO> list(Map<String,Object> map);

    int count(Map<String,Object> map);

    int save(LessonDO lesson);

    int update(LessonDO lesson);

    int remove(int lessonId);

    int batchRemove(int[] lessonIds);

}
