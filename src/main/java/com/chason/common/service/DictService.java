package com.chason.common.service;

import java.util.List;
import java.util.Map;

import com.chason.common.domain.DictDO;
import com.chason.common.domain.Tree;
import com.chason.system.domain.UserDO;

/**
 * 字典表
 */
public interface DictService {

	DictDO get(Long id);

	List<DictDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(DictDO sysDict);

	int update(DictDO sysDict);

	int remove(Long id);

	int batchRemove(Long[] ids);

	List<DictDO> listType();

	String getName(String type,String value);

	/**
	 * 获得DictDo句柄
	 * @param type  类型
	 * @param value 值
	 * */
	DictDO getDictDoByTypeAndValue(String type,String value);

    /**
     * 获得DictDo句柄
     * @param type 类型
     * @param name 名称
     * */
    DictDO getDictDoByTypeAndName(String type,String name);

	/**
	 * 获取爱好列表
	 * @return
     * @param userDO
	 */
	List<DictDO> getHobbyList(UserDO userDO);

	/**
	 * 获取性别列表
 	 * @return
	 */
	List<DictDO> getSexList();

	/**
	 * 根据type获取数据
	 * @param map
	 * @return
	 */
	List<DictDO> listByType(String type);

	/**
	 * 根据type获取tree形数据
	 * */
	Tree<DictDO> getTree(String type);

	/**
     * 根据type获取设备分组树
     * */
	Tree<DictDO> getDeviceGroupTree(String type);

}
