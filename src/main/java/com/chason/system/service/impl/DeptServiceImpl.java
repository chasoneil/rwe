package com.chason.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chason.common.domain.Tree;
import com.chason.common.utils.BuildTree;
import com.chason.system.dao.DeptDao;
import com.chason.system.domain.DeptDO;
import com.chason.system.service.DeptService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao sysDeptMapper;

	@Override
	public DeptDO get(Long deptId){
		return sysDeptMapper.get(deptId);
	}

	@Override
	public List<DeptDO> list(Map<String, Object> map){
		return sysDeptMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return sysDeptMapper.count(map);
	}

	@Override
	public int save(DeptDO sysDept){
		checkDept(sysDept, "add");
		return sysDeptMapper.save(sysDept);
	}

	@Override
	public int update(DeptDO sysDept){
		checkDept(sysDept, "update");
		return sysDeptMapper.update(sysDept);
	}

	@Override
	public int remove(Long deptId){
		return sysDeptMapper.remove(deptId);
	}

	@Override
	public int batchRemove(Long[] deptIds){
		return sysDeptMapper.batchRemove(deptIds);
	}

	@Override
	public Tree<DeptDO> getTree() {
		List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
		List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String,Object>(16));
		for (DeptDO sysDept : sysDepts) {
			Tree<DeptDO> tree = new Tree<DeptDO>();
			tree.setId(sysDept.getDeptId().toString());
			tree.setParentId(sysDept.getParentId().toString());
			tree.setText(sysDept.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DeptDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public boolean checkDeptHasUser(Long deptId) {
		// TODO Auto-generated method stub
		//查询部门以及此部门的下级部门
		int result = sysDeptMapper.getDeptUserNumber(deptId);
		return result == 0;
	}

	private void checkDept(DeptDO deptDO, String action) {

		if(deptDO.getDelFlag() != 1 && deptDO.getDelFlag() != 0) {
			throw new RuntimeException("状态只能是0或者1");
		}

		if (deptDO.getOrderNum() < 0) {
			throw new RuntimeException("排序只能是大于0的正整数");
		}

		if ("add".equals(action)) {
			List<DeptDO> depts = sysDeptMapper.list(new HashMap<>());
			for (DeptDO dept : depts) {
				if (dept.getName().equals(deptDO.getName())) {
					throw new RuntimeException("部门:<" + dept.getName() + ">已经存在");
				}
			}
		}

	}

}
