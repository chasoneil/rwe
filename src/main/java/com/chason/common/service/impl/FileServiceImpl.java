package com.chason.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.chason.common.config.RtmdoConfig;
import com.chason.common.dao.FileDao;
import com.chason.common.domain.FileDO;
import com.chason.common.service.FileService;


@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private FileDao sysFileMapper;

	@Autowired
	private RtmdoConfig rtmdoConfig;
	@Override
	public FileDO get(Long id){
		return sysFileMapper.get(id);
	}

	@Override
	public List<FileDO> list(Map<String, Object> map){
		return sysFileMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return sysFileMapper.count(map);
	}

	@Override
	public int save(FileDO sysFile){
		return sysFileMapper.save(sysFile);
	}

	@Override
	public int update(FileDO sysFile){
		return sysFileMapper.update(sysFile);
	}

	@Override
	public int remove(Long id){
		return sysFileMapper.remove(id);
	}

	@Override
	public int batchRemove(Long[] ids){
		return sysFileMapper.batchRemove(ids);
	}

    @Override
    public Boolean isExist(String url) {
		Boolean isExist = false;
		if (!StringUtils.isEmpty(url)) {
			String filePath = url.replace("/files/", "");
			filePath = rtmdoConfig.getUploadPath() + filePath;
			File file = new File(filePath);
			if (file.exists()) {
				isExist = true;
			}
		}
		return isExist;
	}
	}
