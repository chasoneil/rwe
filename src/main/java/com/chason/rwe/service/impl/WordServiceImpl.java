package com.chason.rwe.service.impl;

import com.chason.rwe.dao.WordDao;
import com.chason.rwe.domain.LessonDO;
import com.chason.rwe.domain.WordDO;
import com.chason.rwe.service.LessonService;
import com.chason.rwe.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordDao wordDao;

    @Autowired
    private LessonService lessonService;

    @Override
    public WordDO get(String id) {
        return wordDao.get(id);
    }

    @Override
    public WordDO findWord(String word, String wordType) {
        return wordDao.findWord(word, wordType);
    }

    @Override
    public List<WordDO> list(Map<String, Object> map) {
        return wordDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return wordDao.count(map);
    }

    @Override
    public int save(WordDO word) {

        LessonDO lessonDO = lessonService.get(word.getLesson());

        if (lessonDO == null) {
            throw new RuntimeException("课程不存在");
        }

        int count = lessonDO.getCount();
        lessonDO.setCount(++count);
        lessonService.update(lessonDO);

        word.setId(UUID.randomUUID().toString());
        word.setCreateTime(new Date());
        return wordDao.save(word);
    }

    @Override
    public int update(WordDO word) {
        return wordDao.update(word);
    }

    @Override
    public int remove(String id) {
        return wordDao.remove(id);
    }

    @Override
    public int batchRemove(String[] ids) {
        return wordDao.batchRemove(ids);
    }
}
