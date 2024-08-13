package com.chason.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chason.common.domain.LogDO;
import com.chason.common.domain.PageDO;
import com.chason.common.utils.Query;
@Service
public interface LogService {

	PageDO<LogDO> queryList(Query query);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<LogDO> list(Map<String, Object> param);
}
