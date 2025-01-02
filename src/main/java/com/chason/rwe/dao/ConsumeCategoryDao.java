package com.chason.rwe.dao;

import com.chason.rwe.domain.ConsumeCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsumeCategoryDao {

    ConsumeCategoryDO get(int id);

    ConsumeCategoryDO getByType(String type);

    List<ConsumeCategoryDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ConsumeCategoryDO consumeCategoryDO);

    int batchSave(List<ConsumeCategoryDO> consumeCategoryDOList);

    int update(ConsumeCategoryDO consumeCategoryDO);

    int updateCategoryName(Map<String, Object> map);

    int remove(int id);

    int removeCategory(String categoryName);

    int batchRemove(int[] ids);

}
