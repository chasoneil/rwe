package com.chason.rwe.service.impl;

import com.chason.rwe.dao.LessonDao;
import com.chason.rwe.domain.LessonDO;
import com.chason.rwe.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao;

    @Override
    public LessonDO get(int lessonId) {
        return lessonDao.get(lessonId);
    }

    @Override
    public List<LessonDO> list(Map<String, Object> map) {
        return lessonDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return lessonDao.count(map);
    }

    @Override
    public int save(LessonDO lesson) {
        return lessonDao.save(lesson);
    }

    @Override
    public int update(LessonDO lesson) {
        return lessonDao.update(lesson);
    }

    @Override
    public int remove(int lessonId) {
        return lessonDao.remove(lessonId);
    }

    @Override
    public int batchRemove(int[] lessonIds) {
        return lessonDao.batchRemove(lessonIds);
    }
}
