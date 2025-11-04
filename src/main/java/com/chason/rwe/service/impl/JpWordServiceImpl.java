package com.chason.rwe.service.impl;

import com.chason.rwe.dao.JpWordDao;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JpWordServiceImpl implements JpWordService {

    @Autowired
    private JpWordDao wordDao;

    @Autowired
    private JpLessonService lessonService;

    @Override
    public JpWordDO get(Integer id) {
        return wordDao.get(id);
    }

    @Override
    public JpWordDO findWord(String word, String wordType) {
        return wordDao.findWord(word, wordType);
    }

    @Override
    public List<JpWordDO> list(Map<String, Object> map) {
        return wordDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return wordDao.count(map);
    }

    @Override
    public int save(JpWordDO word) {

        JpLessonDO lessonDO = lessonService.get(word.getLessonId());

        if (lessonDO == null) {
            throw new RuntimeException("课程不存在");
        }

        int count = lessonDO.getCount();
        lessonDO.setCount(++count);
        // lessonService.update(lessonDO);
        word.setCreateTime(new Date());
        return wordDao.save(word);
    }

    @Override
    public int update(JpWordDO word) {
        return wordDao.update(word);
    }

    @Override
    public int remove(Integer id) {
        return wordDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return wordDao.batchRemove(ids);
    }
}
