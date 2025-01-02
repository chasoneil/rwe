package com.chason.rwe.service.impl;

import com.chason.rwe.dao.ConsumeCategoryDao;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.service.ConsumeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ConsumeCategoryServiceImpl implements ConsumeCategoryService {

    @Autowired
    private ConsumeCategoryDao consumeCategoryDao;

    @Override
    public ConsumeCategoryDO get(int id) {
        return consumeCategoryDao.get(id);
    }

    @Override
    public List<ConsumeCategoryDO> list(Map<String, Object> map) {
        return consumeCategoryDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return consumeCategoryDao.count(map);
    }

    @Override
    public int save(ConsumeCategoryDO consumeCategoryDO) {
        return consumeCategoryDao.save(consumeCategoryDO);
    }

    @Override
    public int batchSave(List<ConsumeCategoryDO> consumeCategoryDOList) {
        return consumeCategoryDao.batchSave(consumeCategoryDOList);
    }

    @Override
    public int update(ConsumeCategoryDO consumeCategoryDO) {
        return consumeCategoryDao.update(consumeCategoryDO);
    }

    @Override
    public int updateCategory(Map<String, Object> map) {
        return consumeCategoryDao.updateCategoryName(map);
    }

    @Override
    public int remove(int id) {
        return consumeCategoryDao.remove(id);
    }

    @Override
    public int removeCategory(ConsumeCategoryDO consumeCategoryDO) {
        String categoryName = consumeCategoryDO.getCategoryName();
        return consumeCategoryDao.removeCategory(categoryName);
    }

    @Override
    public int batchRemove(int[] ids) {
        return consumeCategoryDao.batchRemove(ids);
    }
}
