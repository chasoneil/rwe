package com.chason.rwe.service;

import com.chason.rwe.domain.WordDO;

import java.util.List;
import java.util.Map;

public interface WordService {

    WordDO get(String id);

    WordDO findWord(String word, String wordType);

    List<WordDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(WordDO word);

    int update(WordDO word);

    int remove(String id);

    int batchRemove(String[] ids);

}
