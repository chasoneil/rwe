package com.chason.rwe.dao;

import com.chason.rwe.domain.WordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WordDao {

    WordDO get(String id);

    WordDO findWord(String word, String wordType);

    List<WordDO> list(Map<String,Object> map);

    int count(Map<String,Object> map);

    int save(WordDO word);

    int update(WordDO word);

    int remove(String id);

    int removeByLesson(String lessonId);

    int batchRemove(String[] ids);
}
