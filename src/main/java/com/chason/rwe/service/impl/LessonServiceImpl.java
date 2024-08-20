package com.chason.rwe.service.impl;

import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.LessonDao;
import com.chason.rwe.dao.WordDao;
import com.chason.rwe.domain.LessonDO;
import com.chason.rwe.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private WordDao wordDao;

    @Override
    public LessonDO get(String lessonId) {
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

        if (!StringUtils.isNotNull(lesson.getLesson())) {
            throw new RuntimeException("课程名称不能为空");
        }

        LessonDO lessonDO = lessonDao.findByName(lesson.getLesson());
        if (lessonDO != null) {
            throw new RuntimeException("课程：" + lesson.getLesson() + "已经存在");
        }

        lesson.setCount(0);
        lesson.setLearned(0);
        lesson.setPassed(0);
        lesson.setLearnedTime(0);
        lesson.setLessonId(UUID.randomUUID().toString());
        return lessonDao.save(lesson);
    }

    @Override
    public int update(LessonDO lesson) {
        return lessonDao.update(lesson);
    }

    @Override
    public int remove(String lessonId) {

        LessonDO lessonDO = lessonDao.get(lessonId);
        if (lessonDO == null) {
            throw new RuntimeException("课程不存在");
        }
        wordDao.removeByLesson(lessonId);
        return lessonDao.remove(lessonId);
    }

    @Override
    public int delete(String lesson) {
        LessonDO lessonDO = lessonDao.findByName(lesson);
        if (lessonDO == null) {
            throw new RuntimeException("课程:" + lesson + "不存在");
        }

        wordDao.removeByLesson(lessonDO.getLessonId());
        return lessonDao.remove(lessonDO.getLessonId());
    }

    @Override
    public int batchRemove(String[] lessonIds) {
        return lessonDao.batchRemove(lessonIds);
    }
}
