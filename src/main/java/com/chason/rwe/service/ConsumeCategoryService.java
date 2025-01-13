package com.chason.rwe.service;

import com.chason.rwe.domain.ConsumeCategoryDO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface ConsumeCategoryService {

    ConsumeCategoryDO get(int id);

    ConsumeCategoryDO getByType(String type);

    List<ConsumeCategoryDO> list(Map<String, Object> map);

    List<String> listTypes(String name);

    HashSet<String> listNames(String inOut);

    int count(Map<String, Object> map);

    int save(ConsumeCategoryDO consumeCategoryDO);

    int batchSave(List<ConsumeCategoryDO> consumeCategoryDOList);

    int update(ConsumeCategoryDO consumeCategoryDO);

    int updateCategory(Map<String, Object> map);

    int remove(int id);

    int removeCategory(ConsumeCategoryDO consumeCategoryDO);

    int batchRemove(int[] ids);

    double remainBudget(ConsumeCategoryDO consumeCategoryDO);

}
