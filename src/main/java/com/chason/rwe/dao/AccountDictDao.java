package com.chason.rwe.dao;

import com.chason.rwe.domain.AccountDictDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountDictDao {

    AccountDictDO get(int id);

    List<AccountDictDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(AccountDictDO accountDictDO);

    int update(AccountDictDO accountDictDO);

    int remove(int id);

    int batchRemove(int[] ids);

}
