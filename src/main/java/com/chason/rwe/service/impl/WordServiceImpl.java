package com.chason.rwe.service.impl;

import com.chason.rwe.dao.WordDao;
import com.chason.rwe.domain.WordDO;
import com.chason.rwe.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordDao wordDao;

    @Override
    public WordDO get(int id) {
        return wordDao.get(id);
    }

    @Override
    public List<WordDO> list(Map<String, Object> map) {
        return wordDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return wordDao.count(map);
    }

    @Override
    public int save(WordDO word) {
        return wordDao.save(word);
    }

    @Override
    public int update(WordDO word) {
        return wordDao.update(word);
    }

    @Override
    public int remove(int id) {
        return wordDao.remove(id);
    }

    @Override
    public int batchRemove(int[] ids) {
        return wordDao.batchRemove(ids);
    }
}
