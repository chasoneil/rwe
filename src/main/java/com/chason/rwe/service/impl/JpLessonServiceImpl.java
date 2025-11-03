package com.chason.rwe.service.impl;

import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.JpLessonDao;
import com.chason.rwe.dao.JpWordDao;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.service.JpLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JpLessonServiceImpl implements JpLessonService {

    @Autowired
    private JpLessonDao jpLessonDao;

    @Autowired
    private JpWordDao wordDao;

    @Override
    public JpLessonDO get(Integer id) {
        return jpLessonDao.get(id);
    }

    @Override
    public JpLessonDO find(String lesson) {
        return jpLessonDao.findByName(lesson);
    }

    @Override
    public List<JpLessonDO> list(Map<String, Object> map) {
        return jpLessonDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return jpLessonDao.count(map);
    }

    @Override
    public int save(JpLessonDO lesson) {

        if (!StringUtils.isNotNull(lesson.getLesson())) {
            throw new RuntimeException("课程名称不能为空");
        }

        JpLessonDO lessonDO = jpLessonDao.findByName(lesson.getLesson());
        if (lessonDO != null) {
            throw new RuntimeException("课程：" + lesson.getLesson() + "已经存在");
        }

        lesson.setCount(0);
        lesson.setPassed(0);
        return jpLessonDao.save(lesson);
    }

    @Override
    public int update(JpLessonDO lesson) {
        return jpLessonDao.update(lesson);
    }

    @Override
    public int remove(Integer id) {

        JpLessonDO lessonDO = jpLessonDao.get(id);
        if (lessonDO == null) {
            throw new RuntimeException("课程不存在");
        }
        wordDao.removeByLesson(id);
        return jpLessonDao.remove(id);
    }

    @Override
    public int delete(Integer lesson) {
        JpLessonDO lessonDO = jpLessonDao.get(lesson);
        if (lessonDO == null) {
            throw new RuntimeException("课程:" + lesson + "不存在");
        }

        wordDao.removeByLesson(lessonDO.getId());
        return jpLessonDao.remove(lessonDO.getId());
    }

    @Override
    public int batchRemove(Integer[] lessonIds) {
        return jpLessonDao.batchRemove(lessonIds);
    }
}
