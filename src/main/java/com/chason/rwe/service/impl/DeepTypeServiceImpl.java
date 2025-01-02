package com.chason.rwe.service.impl;

import com.chason.rwe.dao.DeepTypeDao;
import com.chason.rwe.domain.DeepTypeDO;
import com.chason.rwe.service.DeepTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeepTypeServiceImpl implements DeepTypeService {

    @Autowired
    private DeepTypeDao deepTypeDao;

    @Override
    public DeepTypeDO get(int id) {
        return deepTypeDao.get(id);
    }

    @Override
    public List<DeepTypeDO> list(Map<String, Object> map) {
        return deepTypeDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return deepTypeDao.count(map);
    }

    @Override
    public int save(DeepTypeDO deepTypeDO) {
        return deepTypeDao.save(deepTypeDO);
    }

    @Override
    public int batchSave(List<DeepTypeDO> deepTypeDOList) {
        return deepTypeDao.batchSave(deepTypeDOList);
    }

    @Override
    public int update(DeepTypeDO deepTypeDO) {
        return deepTypeDao.update(deepTypeDO);
    }

    @Override
    public int remove(int id) {
        return deepTypeDao.remove(id);
    }

    @Override
    public int batchRemove(int[] ids) {
        return deepTypeDao.batchRemove(ids);
    }
}
