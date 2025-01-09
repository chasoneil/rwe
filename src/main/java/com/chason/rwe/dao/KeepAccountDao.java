package com.chason.rwe.dao;

import com.chason.rwe.domain.KeepAccountDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface KeepAccountDao {

    KeepAccountDO get(int id);

    List<KeepAccountDO> list(Map<String, Object> map);

    List<KeepAccountDO> listByTime(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(KeepAccountDO keepAccountDO);

    int batchSave(List<KeepAccountDO> keepAccountDOList);

    int update(KeepAccountDO keepAccountDO);

    int remove(int id);

    int batchRemove(int[] ids);
}
