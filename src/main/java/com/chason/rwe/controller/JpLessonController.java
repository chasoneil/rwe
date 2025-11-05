package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rwe/jp/lesson")
public class JpLessonController extends BaseController {

    private static final String PREFIX = "rwe/jp/lesson";

    @Autowired
    private JpLessonService jpLessonService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/lesson")
    String lessons(Model model) {
        List<JpLessonDO> lessons = jpLessonService.list(new HashMap<>());
        model.addAttribute("lessons", lessons);
        return PREFIX + "/lesson";
    }

    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, Object> params) {

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);
        params.putIfAbsent("userId", getUserId());

        Query query = new Query(params);
        List<JpLessonDO> lessons = jpLessonService.list(query);
        int total = jpLessonService.count(query);
        return new PageUtils(lessons, total);
    }

    @GetMapping("/add")
    String add() {
        return PREFIX + "/add";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("lesson", jpLessonService.get(id));
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/save")
    R save(JpLessonDO lesson) {
        try {
            lesson.setUserId(getUserId());
            jpLessonService.save(lesson);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/update")
    R update(JpLessonDO jpLessonDO) {

        try {
            jpLessonService.update(jpLessonDO, getUserId());
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/remove")
    R remove(Integer id) {
        try {
            Long userId = getUserId();
            jpLessonService.remove(id, userId);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

}
