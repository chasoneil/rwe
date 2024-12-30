package com.chason.rwe.service;

import com.chason.rwe.domain.KeepAccountDO;

import java.util.List;
import java.util.Map;

public interface KeepAccountService {

    KeepAccountDO get(String id);

    List<KeepAccountDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(KeepAccountDO keepAccountDO);

    int batchSave(List<KeepAccountDO> keepAccountDOList);

    int update(KeepAccountDO keepAccountDO);

    int remove(int id);

    int batchRemove(int[] ids);
}
