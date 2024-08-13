package com.chason.rwe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.rwe.dao.WhiteListDao;
import com.chason.rwe.domain.WhiteListDO;
import com.chason.rwe.service.WhiteListService;

import java.util.List;
import java.util.Map;



@Service
public class WhiteListServiceImpl implements WhiteListService {

	@Autowired
	private WhiteListDao whiteListDao;

	@Override
	public WhiteListDO get(String whId){
		return whiteListDao.get(whId);
	}

	@Override
	public List<WhiteListDO> list(Map<String, Object> map){
		return whiteListDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return whiteListDao.count(map);
	}

	@Override
	public int save(WhiteListDO whiteList){
		return whiteListDao.save(whiteList);
	}

	@Override
	public int update(WhiteListDO whiteList){
		return whiteListDao.update(whiteList);
	}

	@Override
	public int remove(String whId){
		return whiteListDao.remove(whId);
	}

	@Override
	public int batchRemove(String[] whIds){
		return whiteListDao.batchRemove(whIds);
	}

}
