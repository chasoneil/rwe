package com.chason.rwe.service;

import java.util.List;
import java.util.Map;

import com.chason.rwe.domain.DeviceDO;

/**
 *
 *
 * @author huzq
 * @email huzq@biaokaow.com
 * @date 2018-05-29 10:09:59
 */
public interface DeviceService {

	DeviceDO get(String devId);

    DeviceDO findByNumber(String devNumber);

    DeviceDO findByDevCode(String devCode);

	List<DeviceDO> list(Map<String, Object> map);

	List<DeviceDO> listValid(Map<String, Object> map);

	List<DeviceDO> findAllValidByGroupCode(String groupCode);

	List<DeviceDO> findAllByStatus(String devStatus);

	int count(Map<String, Object> map);

	int save(DeviceDO device);

	int update(DeviceDO device);

	int remove(String devId);

	int batchRemove(String[] devIds);

	String getDevTypeByGroupCode(String devGroupCode);
}
