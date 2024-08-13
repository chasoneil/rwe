package com.chason.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.chason.common.domain.LogDO;

@Mapper
public interface LogDao {

	LogDO get(Long id);

	List<LogDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(LogDO log);

	int update(LogDO log);

	int remove(Long id);

	int batchRemove(Long[] ids);
}
