package com.chason.rwe.service;

import com.chason.rwe.domain.WordDO;

import java.util.List;
import java.util.Map;

public interface WordService {

    WordDO get(int id);

    List<WordDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(WordDO word);

    int update(WordDO word);

    int remove(int id);

    int batchRemove(int[] ids);

}
