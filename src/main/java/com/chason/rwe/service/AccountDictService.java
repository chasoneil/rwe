package com.chason.rwe.service;

import com.chason.rwe.domain.AccountDictDO;
import java.util.List;
import java.util.Map;

public interface AccountDictService {

    AccountDictDO get(int id);

    List<AccountDictDO> list(Map<String, Object> map);

    boolean exist(AccountDictDO accountDictDO);

    int count(Map<String, Object> map);

    int save(AccountDictDO accountDictDO);

    int update(AccountDictDO accountDictDO);

    int remove(int id);

    int batchRemove(int[] ids);
}
