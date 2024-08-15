package com.chason.rwe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.rwe.dao.DeviceDao;
import com.chason.rwe.domain.DeviceDO;
import com.chason.rwe.service.DeviceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceDao deviceDao;

	@Override
	public DeviceDO get(String devId){
		return deviceDao.get(devId);
	}

    @Override
    public DeviceDO findByNumber(String devNumber){
        return deviceDao.findByNumber(devNumber);
    }

    @Override
    public DeviceDO findByDevCode(String devCode){
        return deviceDao.findByDevCode(devCode);
    }

	@Override
	public List<DeviceDO> list(Map<String, Object> map){
		return deviceDao.list(map);
	}

	@Override
	public List<DeviceDO> listValid(Map<String, Object> map){
	    return deviceDao.listValid(map);
	}

    @Override
    public List<DeviceDO> findAllValidByGroupCode(String groupCodeOrNull){
        Map<String, Object> params = new HashMap<>(16);
        params.put("devGroupCode", groupCodeOrNull);
        return deviceDao.listValid(params);
    }

    @Override
    public List<DeviceDO> findAllByStatus(String devStatus)
    {
        Map<String, Object> params = new HashMap<>(16);
        params.put("devStatus", devStatus);
        return deviceDao.list(params);
    }

	@Override
	public int count(Map<String, Object> map){
		return deviceDao.count(map);
	}

	@Override
	public int save(DeviceDO device){
		return deviceDao.save(device);
	}

	@Override
	public int update(DeviceDO device){
		return deviceDao.update(device);
	}

	@Override
	public int remove(String devId){
		return deviceDao.remove(devId);
	}

	@Override
	public int batchRemove(String[] devIds){
		return deviceDao.batchRemove(devIds);
	}

	@Override
	public String getDevTypeByGroupCode(String devGroupCode)
	{
	    return deviceDao.getDevTypeByGroupCode(devGroupCode);
	}
}
