package com.chason.rwe.dao;

import com.chason.rwe.domain.SpaceDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author chason
 */
@Mapper
public interface SpaceDao {

	SpaceDO get(String spaceId);

	SpaceDO findByCode(String spaceCode);

	List<SpaceDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(SpaceDO space);

	int update(SpaceDO space);

	int remove(String space_id);

	int batchRemove(String[] spaceIds);
}
