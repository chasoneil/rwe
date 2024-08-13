package com.chason.rwe.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.chason.rwe.domain.PolicyDO;

/**
 * @author chason
 */
@Mapper
public interface PolicyDao {

	PolicyDO get(String policyId);

	List<PolicyDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(PolicyDO policy);

	int update(PolicyDO policy);

	int remove(String policyId);

	int batchRemove(String[] policyIds);

}
