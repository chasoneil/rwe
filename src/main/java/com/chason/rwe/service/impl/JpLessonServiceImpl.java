package com.chason.rwe.service.impl;

import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.JpLessonDao;
import com.chason.rwe.dao.JpWordDao;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.service.JpLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JpLessonServiceImpl implements JpLessonService {

    @Autowired
    private JpLessonDao jpLessonDao;

    @Autowired
    private JpWordDao jpWordDao;

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

        if (map.get("userId") == null) {
            return new ArrayList<>();
        }

        long userId = (long) map.get("userId");
        // admin
        if (userId == 1) {
            map.remove("userId");
        }

        return jpLessonDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {

        if (map.get("userId") == null) {
            return 0;
        }

        long userId = (long) map.get("userId");
        if (userId == 1) {
            map.remove("userId");
        }

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
    public int update(JpLessonDO lesson, Long userId) {

        if (!checkPrivilege(lesson, userId)) {
            throw new RuntimeException("权限校验失败");
        }

        if (StringUtils.isEmpty(lesson.getLesson())) {
            throw new RuntimeException("课程名称不能为空");
        }

        JpLessonDO jpLessonDO = jpLessonDao.get(lesson.getId());
        if (lesson.getLesson().equals(jpLessonDO.getLesson())) {        // 相同的名称
            return 1;
        }

        JpLessonDO existLesson = jpLessonDao.findByName(lesson.getLesson());
        if (existLesson != null) {
            throw new RuntimeException("已存在该课程名");
        }

        jpLessonDO.setLesson(lesson.getLesson());
        return jpLessonDao.update(jpLessonDO);
    }

    @Override
    public int update(JpLessonDO jpLessonDO) {
        return jpLessonDao.update(jpLessonDO);
    }

    @Override
    public int remove(Integer id, Long userId) {

        JpLessonDO jpLessonDO = jpLessonDao.get(id);
        if (jpLessonDO == null) {
            throw new RuntimeException("课程不存在");
        }
        if (!checkPrivilege(jpLessonDO, userId)) {
            throw new RuntimeException("权限校验失败，无法删除该课程");
        }
        jpWordDao.removeByLesson(id);
        return jpLessonDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] lessonIds) {
        return jpLessonDao.batchRemove(lessonIds);
    }

    private boolean checkPrivilege(JpLessonDO jpLessonDO, Long userId) {

        if (userId == 1 || jpLessonDO.getUserId() == userId) {
            return true;
        }

        return false;
    }
}
