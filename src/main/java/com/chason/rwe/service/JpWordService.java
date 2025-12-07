package com.chason.rwe.service;

import com.chason.rwe.domain.JpWordDO;

import java.util.List;
import java.util.Map;

public interface JpWordService {

    JpWordDO get(Integer id);

    JpWordDO findWord(String word, String wordType);

    List<JpWordDO> list(Map<String, Object> map);

    List<JpWordDO> listNoLearned(Map<String, Object> map);

    boolean checkExist(String word, String wordVoice);

    int count(Map<String, Object> map);

    int save(JpWordDO word);

    int update(JpWordDO word);

    int learnWord(JpWordDO word);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    void updateRem(String data);

}
