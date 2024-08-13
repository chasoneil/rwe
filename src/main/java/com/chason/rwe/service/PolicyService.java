package com.chason.rwe.service;

import java.util.List;
import java.util.Map;

import com.chason.rwe.domain.PolicyDO;

/**
 * @author chason
 */
public interface PolicyService {

	PolicyDO get(String policyId);

	List<PolicyDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(PolicyDO policy);

	int update(PolicyDO policy);

	int remove(String policyId);

	int batchRemove(String[] policyIds);

}
