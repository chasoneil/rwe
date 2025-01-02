package com.chason.rwe.dao;

import com.chason.rwe.domain.DeepTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeepTypeDao {

    DeepTypeDO get(int id);

    List<DeepTypeDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(DeepTypeDO deepTypeDO);

    int batchSave(List<DeepTypeDO> deepTypeDOList);

    int update(DeepTypeDO deepTypeDO);

    int remove(int id);

    int batchRemove(int[] ids);

}
