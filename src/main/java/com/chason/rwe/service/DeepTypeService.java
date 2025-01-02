package com.chason.rwe.service;

import com.chason.rwe.domain.DeepTypeDO;

import java.util.List;
import java.util.Map;

public interface DeepTypeService {

    DeepTypeDO get(int id);

    List<DeepTypeDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(DeepTypeDO deepTypeDO);

    int batchSave(List<DeepTypeDO> deepTypeDOList);

    int update(DeepTypeDO deepTypeDO);

    int remove(int id);

    int batchRemove(int[] ids);

}
