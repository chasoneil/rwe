package com.chason.rwe.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.chason.rwe.domain.DeviceDO;

/**
 *
 * @author huzq
 * @email zzb@zzb.com
 * @date 2018-05-29 10:09:59
 */
@Mapper
public interface DeviceDao {

	DeviceDO get(String devId);

    DeviceDO findByNumber(String devNumber);

    DeviceDO findByDevCode(String devCode);

	List<DeviceDO> list(Map<String,Object> map);

	List<DeviceDO> listValid(Map<String, Object> map);

	int count(Map<String,Object> map);

	int save(DeviceDO device);

	int update(DeviceDO device);

	int remove(String dev_id);

	int batchRemove(String[] devIds);

	String getDevTypeByGroupCode(String devGroupCode);
}
