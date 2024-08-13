package com.chason.rwe.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.chason.rwe.domain.WhiteListDO;

/**
 *
 * @author huzq
 * @email zzb@zzb.com
 * @date 2018-05-29 10:09:59
 */
@Mapper
public interface WhiteListDao {

	WhiteListDO get(String whId);

	List<WhiteListDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(WhiteListDO whiteList);

	int update(WhiteListDO whiteList);

	int remove(String whId);

	int batchRemove(String[] whIds);
}
