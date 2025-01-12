package com.chason.common.dict;

import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.service.ConsumeCategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 与记账功能相关的字典缓存
 * @author Chason
 */
@Service
public class AccountDict {

    private static final AccountDict accountDict = new AccountDict();

    public static AccountDict getInstance() {
        return accountDict;
    }

    private Map<Long, HashSet<String>> CATEGORY_NAME_DICT = new HashMap<>();

    private Map<Long, HashSet<String>> CATEGORY_TYPE_DICT = new HashMap<>();

    public Map<Long, HashSet<String>> getCategoryNameDict(ConsumeCategoryService consumeCategoryService,int roleLevel,
                                                          long userId) {
        if (userId == 0) {
            return null;
        }

        if (CATEGORY_NAME_DICT.isEmpty() || CATEGORY_NAME_DICT.get(userId).isEmpty()) {
            initCategoryNameDict(consumeCategoryService, roleLevel, userId);
        }

        return CATEGORY_NAME_DICT;
    }

    public Map<Long, HashSet<String>> getCategoryTypeDict(ConsumeCategoryService consumeCategoryService,
                                                          int roleLevel, long userId) {
        if (userId == 0) {
            return null;
        }

        if (CATEGORY_TYPE_DICT.isEmpty() || CATEGORY_TYPE_DICT.get(userId).isEmpty()) {
            initCategoryTypeDict(consumeCategoryService, roleLevel, userId);
        }

        return CATEGORY_TYPE_DICT;
    }

    public void initCategoryNameDict(ConsumeCategoryService consumeCategoryService, int roleLevel,
                                     long userId) {
        Map<String, Object> param = new HashMap<>();
        if (roleLevel == 20 && userId != 0) {
            param.put("userId", userId);
        }
        List<ConsumeCategoryDO> list = consumeCategoryService.list(param);
        HashSet<String> dicts = CATEGORY_NAME_DICT.get(userId);
        if (dicts == null) {
            dicts = new HashSet<>();
        } else {
            dicts.clear();
        }
        for (ConsumeCategoryDO categoryDO : list) {
            if ("-".equals(categoryDO.getCategoryName())) {
                continue;
            }
            dicts.add(categoryDO.getCategoryName());
        }
        CATEGORY_NAME_DICT.put(userId, dicts);
    }

    public void initCategoryTypeDict(ConsumeCategoryService consumeCategoryService, int roleLevel,
                                     long userId) {
        Map<String, Object> param = new HashMap<>();
        if (roleLevel == 20 && userId != 0) {
            param.put("userId", userId);
        }
        List<ConsumeCategoryDO> list = consumeCategoryService.list(new HashMap<>());
        HashSet<String> dicts = CATEGORY_TYPE_DICT.get(userId);
        if (dicts == null) {
            dicts = new HashSet<>();
        } else {
            dicts.clear();
        }

        for (ConsumeCategoryDO categoryDO : list) {
            if ("-".equals(categoryDO.getCategoryType())) {
                continue;
            }
            dicts.add(categoryDO.getCategoryType());
        }

        CATEGORY_TYPE_DICT.put(userId, dicts);
    }
}
