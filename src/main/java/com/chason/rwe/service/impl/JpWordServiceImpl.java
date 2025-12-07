package com.chason.rwe.service.impl;

import com.alibaba.fastjson.JSON;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.JpWordDao;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<JpWordDO> listNoLearned(Map<String, Object> map) {
        List<JpWordDO> list = list(map);
        Iterator<JpWordDO> iterator = list.iterator();
        while (iterator.hasNext()) {
            JpWordDO wordDO = iterator.next();
            if (wordDO.getLearned() == 2) {
                iterator.remove();
            }
        }
        return list;
    }

    @Override
    public boolean checkExist(String word, String wordVoice) {
        JpWordDO jpWordDO = jpWordDao.checkExist(word, wordVoice);
        return jpWordDO != null;
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

        if (checkExist(jpWordDO.getWord(), jpWordDO.getWordVoice())) {
            throw new RuntimeException("单词已存在");
        }

        int count = lessonDO.getCount();
        lessonDO.setCount(++count);
        jpLessonService.update(lessonDO);

        if (StringUtils.isEmpty(jpWordDO.getWordCn())) {
            jpWordDO.setWordCn(jpWordDO.getWord());
        }

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
    @Transactional
    public int learnWord(JpWordDO word) {
        Date time = new Date();
        word.setLastReviewTime(time);
        JpLessonDO lessonDO = jpLessonService.get(word.getLessonId());
        if (word.getLearned() == 2) {
            int passed = lessonDO.getPassed();
            lessonDO.setPassed(++passed);
        }
        lessonDO.setLastLearnTime(time);
        jpLessonService.update(lessonDO);
        return update(word);
    }

    @Override
    @Transactional
    public int remove(Integer id) {
        JpWordDO jpWordDO = jpWordDao.get(id);
        JpLessonDO jpLessonDO = jpLessonService.get(jpWordDO.getLessonId());
        jpLessonDO.setCount(jpLessonDO.getCount()-1);
        jpLessonService.update(jpLessonDO);
        return jpWordDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return jpWordDao.batchRemove(ids);
    }

    @Override
    @Transactional
    public void updateRem(String data) {

        List<JpWordDO> words = JSON.parseArray(data, JpWordDO.class);
        Date d = new Date();
        for (JpWordDO jpWordDO : words) {
            jpWordDO.setLastReviewTime(d);
            jpWordDao.update(jpWordDO);
        }

        if (!words.isEmpty()) {
            JpLessonDO lessonDO = jpLessonService.get(words.get(0).getLessonId());
            lessonDO.setLastLearnTime(d);
            jpLessonService.update(lessonDO);
        }
    }
}
