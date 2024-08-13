package com.chason.rwe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.rwe.dao.PolicyDao;
import com.chason.rwe.domain.PolicyDO;
import com.chason.rwe.service.PolicyService;

import java.util.List;
import java.util.Map;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyDao policyDao;

	@Override
	public PolicyDO get(String policyId){
		return policyDao.get(policyId);
	}

	@Override
	public List<PolicyDO> list(Map<String, Object> map){
		return policyDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return policyDao.count(map);
	}

	@Override
	public int save(PolicyDO policy){
		return policyDao.save(policy);
	}

	@Override
	public int update(PolicyDO policy){
		return policyDao.update(policy);
	}

	@Override
	public int remove(String policyId){
		return policyDao.remove(policyId);
	}

	@Override
	public int batchRemove(String[] policyIds){
		return policyDao.batchRemove(policyIds);
	}
}
