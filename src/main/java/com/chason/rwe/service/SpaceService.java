package com.chason.rwe.service;

import com.chason.common.domain.Tree;
import com.chason.rwe.domain.SpaceDO;
import com.chason.system.domain.UserDO;

import java.util.List;
import java.util.Map;

public interface SpaceService {

	SpaceDO get(String spaceId);

	SpaceDO findByCode(String spaceCode);

	List<SpaceDO> list(Map<String, Object> map);

    List<SpaceDO> listByLikeManager(UserDO user);

	int count(Map<String, Object> map);

	int save(SpaceDO space);

	int update(SpaceDO space);

	int remove(String spaceId);

	int batchRemove(String[] spaceIds);

	boolean codeIsUnique(String code);

	/**
	 * 获得指定用户的树
	 * @param user
	 * @return
	 */
	Tree<SpaceDO> getTreeByUser(UserDO user);

    /**
     * 获取根据管理员名称获取tree形数据
     * */
    Tree<SpaceDO> getTree(UserDO user);

    /**
     * 获取最末梢节点
     * @return
     */
    List<String> getTerminal(List<String> spaceIds);
}
