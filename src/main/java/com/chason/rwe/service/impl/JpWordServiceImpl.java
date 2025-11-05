package com.chason.rwe.service.impl;

import com.chason.rwe.dao.JpWordDao;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JpWordServiceImpl implements JpWordService {

    @Autowired
    private JpWordDao jpWordDao;

    @Autowired
    private JpLessonService jpLessonService;

    @Override
    public JpWordDO get(Integer id) {
        return jpWordDao.get(id);
    }

    @Override
    public JpWordDO findWord(String word, String wordType) {
        return jpWordDao.findWord(word, wordType);
    }

    @Override
    public List<JpWordDO> list(Map<String, Object> map) {
        return jpWordDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return jpWordDao.count(map);
    }

    @Override
    @Transactional
    public int save(JpWordDO jpWordDO) {

        JpLessonDO lessonDO = jpLessonService.get(jpWordDO.getLessonId());
        if (lessonDO == null) {
            throw new RuntimeException("课程不存在");
        }

        int count = lessonDO.getCount();
        lessonDO.setCount(++count);
        jpLessonService.update(lessonDO);

        jpWordDO.setLearned(0);
        jpWordDO.setLearnTime(0);
        jpWordDO.setCreateTime(new Date());
        return jpWordDao.save(jpWordDO);
    }

    @Override
    public int update(JpWordDO word) {
        return jpWordDao.update(word);
    }

    @Override
    public int remove(Integer id) {
        return jpWordDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return jpWordDao.batchRemove(ids);
    }
}
