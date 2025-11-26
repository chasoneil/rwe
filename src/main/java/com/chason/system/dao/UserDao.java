package com.chason.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.chason.system.domain.UserDO;

@Mapper
public interface UserDao {

	UserDO get(Long userId);

    UserDO getByName(String username);

	List<UserDO> list(Map<String,Object> map);

	int count(Map<String,Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);

	int batchRemove(Long[] userIds);

	Long[] listAllDept();

}
