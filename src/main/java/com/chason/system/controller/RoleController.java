package com.chason.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chason.common.annotation.Log;
import com.chason.common.controller.BaseController;
import com.chason.common.utils.R;
import com.chason.system.domain.RoleDO;
import com.chason.system.service.RoleService;

import java.util.List;

@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {

	private static final String PREFIX = "system/role";

	@Autowired
	private RoleService roleService;

	@RequiresPermissions("sys:role:role")
	@GetMapping()
	public String role() {
		return PREFIX + "/role";
	}

	@RequiresPermissions("sys:role:role")
	@GetMapping("/list")
	@ResponseBody()
	public List<RoleDO> list() {
		List<RoleDO> roles = roleService.list();
		return roles;
	}

	@Log("添加角色")
	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	public String add() {
		return PREFIX + "/add";
	}

	@Log("编辑角色")
	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		RoleDO roleDO = roleService.get(id);
		model.addAttribute("role", roleDO);
		return PREFIX + "/edit";
	}

	@Log("保存角色")
	@RequiresPermissions("sys:role:add")
	@PostMapping("/save")
	@ResponseBody()
	public R save(RoleDO role) {
		if (roleService.save(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@Log("更新角色")
	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	public R update(RoleDO role) {
		if (roleService.update(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@Log("删除角色")
	@RequiresPermissions("sys:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	public R save(Long id) {
		if (roleService.remove(id) > 0) {
			return R.ok();
		} else {
			return R.error(1, "删除失败");
		}
	}

	@RequiresPermissions("sys:role:batchRemove")
	@Log("批量删除角色")
	@PostMapping("/batchRemove")
	@ResponseBody
	public R batchRemove(@RequestParam("ids[]") Long[] ids) {
		int r = roleService.batchremove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}
}
