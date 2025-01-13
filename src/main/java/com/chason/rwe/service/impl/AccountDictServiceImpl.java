package com.chason.rwe.service.impl;

import com.chason.rwe.dao.AccountDictDao;
import com.chason.rwe.domain.AccountDictDO;
import com.chason.rwe.service.AccountDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountDictServiceImpl implements AccountDictService {

    @Autowired
    private AccountDictDao accountDictDao;

    @Override
    public AccountDictDO get(int id) {
        return accountDictDao.get(id);
    }

    @Override
    public List<AccountDictDO> list(Map<String, Object> map) {
        return accountDictDao.list(map);
    }

    @Override
    public boolean exist(AccountDictDO accountDictDO) {

        Map<String, Object> params = new HashMap<>();
        List<AccountDictDO> dictDOS = accountDictDao.list(params);

        for (AccountDictDO dictDO : dictDOS) {
            if ( dictDO.getDictName().equalsIgnoreCase(accountDictDO.getDictName())
                    && dictDO.getDictValue().equalsIgnoreCase(accountDictDO.getDictValue())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int count(Map<String, Object> map) {
        return accountDictDao.count(map);
    }

    @Override
    public int save(AccountDictDO accountDictDO) {
        return accountDictDao.save(accountDictDO);
    }

    @Override
    public int update(AccountDictDO accountDictDO) {
        return accountDictDao.update(accountDictDO);
    }

    @Override
    public int remove(int id) {
        return accountDictDao.remove(id);
    }

    @Override
    public int batchRemove(int[] ids) {
        return accountDictDao.batchRemove(ids);
    }
}
