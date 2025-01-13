package com.chason.rwe.service.impl;

import com.chason.common.utils.StringUtils;
import com.chason.rwe.dao.ConsumeCategoryDao;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.service.ConsumeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsumeCategoryServiceImpl implements ConsumeCategoryService {

    @Autowired
    private ConsumeCategoryDao consumeCategoryDao;

    @Override
    public ConsumeCategoryDO get(int id) {
        return consumeCategoryDao.get(id);
    }

    @Override
    public ConsumeCategoryDO getByType(String type) {
        return consumeCategoryDao.getByType(type);
    }

    @Override
    public List<ConsumeCategoryDO> list(Map<String, Object> map) {
        return consumeCategoryDao.list(map);
    }

    @Override
    public List<String> listTypes(String name) {
        List<String> types = new ArrayList<>();
        if (StringUtils.isNotNull(name)) {
            Map<String, Object> param = new HashMap<>();
            param.put("categoryName", name);
            List<ConsumeCategoryDO> list = consumeCategoryDao.list(param);
            if (list != null && !list.isEmpty()) {
                for (ConsumeCategoryDO categoryDO : list) {
                    if ("-".equals(categoryDO.getCategoryType())) {
                        continue;
                    }
                    types.add(categoryDO.getCategoryType());
                }
            }
        }
        return types;
    }

    @Override
    public HashSet<String> listNames(String inOut) {

        HashSet<String> names = new HashSet<>();
        if (StringUtils.isNotNull(inOut)) {
            Map<String, Object> param = new HashMap<>();
            param.put("inOut", inOut);
            List<ConsumeCategoryDO> list = consumeCategoryDao.list(param);
            if (list != null && !list.isEmpty()) {
                for (ConsumeCategoryDO categoryDO : list) {
                    names.add(categoryDO.getCategoryName());
                }
            }
        }

        return names;
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

    /**
     * if consumeCategory is first level category, return son's sum of budget,
     * else return parent's budget - son's sum of budget
     * @param consumeCategoryDO
     * @return
     */
    @Override
    public double remainBudget(ConsumeCategoryDO consumeCategoryDO) {

        Map<String, Object> param = new HashMap<>();
        double sonBudget = 0;

        if (consumeCategoryDO.getLevel() == 1) {
            param.put("parentId", consumeCategoryDO.getId());
        } else if (consumeCategoryDO.getLevel() == 2) {
            param.put("parentId", consumeCategoryDO.getParentId());
        }

        List<ConsumeCategoryDO> list = consumeCategoryDao.list(param);
        for (ConsumeCategoryDO categoryDO : list) {
            sonBudget += categoryDO.getBudget();
        }

        if (consumeCategoryDO.getLevel() == 1) {
            return sonBudget;
        } else if (consumeCategoryDO.getLevel() == 2) {
            ConsumeCategoryDO parent = consumeCategoryDao.get(consumeCategoryDO.getParentId());
            return parent.getBudget() - sonBudget;
        }

        return 0;
    }
}
