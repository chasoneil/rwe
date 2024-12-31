package com.chason.rwe.service.impl;

import com.chason.rwe.dao.KeepAccountDao;
import com.chason.rwe.domain.KeepAccountDO;
import com.chason.rwe.service.KeepAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KeepAccountServiceImpl implements KeepAccountService {

    @Autowired
    private KeepAccountDao keepAccountDao;

    @Override
    public KeepAccountDO get(int id) {
        return keepAccountDao.get(id);
    }

    @Override
    public List<KeepAccountDO> list(Map<String, Object> map) {
        return keepAccountDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return keepAccountDao.count(map);
    }

    @Override
    public int save(KeepAccountDO keepAccountDO) {
        return keepAccountDao.save(keepAccountDO);
    }

    @Override
    public int batchSave(List<KeepAccountDO> keepAccountDOList) {
        return keepAccountDao.batchSave(keepAccountDOList);
    }

    @Override
    public int update(KeepAccountDO keepAccountDO) {
        return keepAccountDao.update(keepAccountDO);
    }

    @Override
    public int remove(int id) {
        return keepAccountDao.remove(id);
    }

    @Override
    public int batchRemove(int[] ids) {
        return keepAccountDao.batchRemove(ids);
    }
}
