package com.chason.rwe.dao;

import com.chason.rwe.domain.JpWordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JpWordDao {

    JpWordDO get(Integer id);

    JpWordDO findWord(String word, String wordType);

    List<JpWordDO> list(Map<String,Object> map);

    int count(Map<String,Object> map);

    int save(JpWordDO word);

    int update(JpWordDO word);

    int remove(Integer id);

    int removeByLesson(Integer lessonId);

    int batchRemove(Integer[] ids);
}
