package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.page.TradePage;
import com.chason.rwe.service.JpLessonService;
import com.chason.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rwe/jp/lesson")
public class LessonController extends BaseController {

    private static final String PREFIX = "rwe/jp/lesson";

    @Autowired
    private JpLessonService jpLessonService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/lesson")
    public String lessons(Model model) {
        List<JpLessonDO> lessons = jpLessonService.list(new HashMap<>());
        model.addAttribute("lessons", lessons);
        return PREFIX + "/lesson";
    }

    @GetMapping("/list")
    @ResponseBody
    public PageUtils list(@RequestParam Map<String, Object> params) {

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);
        params.putIfAbsent("userId", getUserId());

        Query query = new Query(params);
        List<JpLessonDO> lessons = jpLessonService.list(query);
        int total = jpLessonService.count(query);
        return new PageUtils(lessons, total);
    }

    @GetMapping("/add")
    @RequiresPermissions("rwe:lesson:add")
    public String add() {
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rwe:lesson:add")
    public R save(JpLessonDO lesson) {

        try {
            int save = jpLessonService.save(lesson);
            if (save != 1) {
                return R.error("新增课程失败");
            }
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/remove")
    @RequiresPermissions("rwe:lesson:delete")
    public R remove(Integer lessonId) {
        try {
            int result = jpLessonService.remove(lessonId);
            if (result > 0) {
                return R.ok();
            }
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.error();
    }

}
