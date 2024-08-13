package com.chason.rwe.service;

import java.util.List;
import java.util.Map;

import com.chason.rwe.domain.WhiteListDO;

/**
 *
 *
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */
public interface WhiteListService {

	WhiteListDO get(String whId);

	List<WhiteListDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WhiteListDO whiteList);

	int update(WhiteListDO whiteList);

	int remove(String whId);

	int batchRemove(String[] whIds);
}
