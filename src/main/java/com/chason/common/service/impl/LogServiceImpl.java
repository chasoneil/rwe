package com.chason.common.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.common.dao.LogDao;
import com.chason.common.domain.LogDO;
import com.chason.common.domain.PageDO;
import com.chason.common.service.LogService;
import com.chason.common.utils.Query;

@SuppressWarnings("AlibabaRemoveCommentedCode")
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	LogDao logMapper;

	@Override
	public PageDO<LogDO> queryList(Query query) {
		int total = logMapper.count(query);
		List<LogDO> logs = logMapper.list(query);
		PageDO<LogDO> page = new PageDO<>();
		page.setTotal(total);
		page.setRows(logs);
		return page;
	}

	@Override
	public int remove(Long id) {
		return logMapper.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids) {
		return logMapper.batchRemove(ids);
	}

    @Override
    public int removeBeforeDate(Date date) {
        return logMapper.removeBeforeDate(date);
    }

    @Override
    public List<LogDO> list(Map<String, Object> param) {
        return logMapper.list(param);
    }
}
