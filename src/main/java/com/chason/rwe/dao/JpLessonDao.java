package com.chason.rwe.dao;

import com.chason.rwe.domain.JpLessonDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpLessonDao {

    JpLessonDO get(Integer id);

    List<JpLessonDO> list(Map<String,Object> map);

    JpLessonDO findByName(String lesson);

    int count(Map<String,Object> map);

    int save(JpLessonDO lesson);

    int update(JpLessonDO lesson);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

}
