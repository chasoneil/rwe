package com.chason.common.service;

import java.util.List;
import java.util.Map;

import com.chason.common.domain.FileDO;

/**
 * 文件上传
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
public interface FileService {

	FileDO get(Long id);

	List<FileDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(FileDO sysFile);

	int update(FileDO sysFile);

	int remove(Long id);

	int batchRemove(Long[] ids);

	/**
	 * 判断一个文件是否存在
	 * @param url FileDO中存的路径
	 * @return
	 */
    Boolean isExist(String url);
}
