package com.chason.common.dict;

import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.domain.DeepTypeDO;
import com.chason.rwe.service.ConsumeCategoryService;
import com.chason.rwe.service.DeepTypeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    private HashSet<String> DEEP_TYPE_DICT = new HashSet<>();

    private HashSet<String> CATEGORY_NAME_DICT = new HashSet<>();

    public HashSet<String> getDeepTypeDict(DeepTypeService deepTypeService) {

        if (DEEP_TYPE_DICT.isEmpty()) {
            initDeepTypeDict(deepTypeService);
        }

        return DEEP_TYPE_DICT;
    }

    public HashSet<String> getCategoryNameDict(ConsumeCategoryService consumeCategoryService) {

        if (CATEGORY_NAME_DICT.isEmpty()) {
            initCategoryNameDict(consumeCategoryService);
        }

        return CATEGORY_NAME_DICT;
    }

    public void initDeepTypeDict(DeepTypeService deepTypeService) {
        List<DeepTypeDO> list = deepTypeService.list(new HashMap<>());
        DEEP_TYPE_DICT.clear();
        for (DeepTypeDO deepTypeDO : list) {
            DEEP_TYPE_DICT.add(deepTypeDO.getDeepTypeName());
        }
    }

    public void initCategoryNameDict(ConsumeCategoryService consumeCategoryService) {
        List<ConsumeCategoryDO> list = consumeCategoryService.list(new HashMap<>());
        for (ConsumeCategoryDO categoryDO : list) {
            CATEGORY_NAME_DICT.add(categoryDO.getCategoryName());
        }
    }



}
