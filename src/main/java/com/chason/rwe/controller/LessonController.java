package com.chason.rwe.controller;

import com.chason.common.utils.R;
import com.chason.rwe.domain.LessonDO;
import com.chason.rwe.service.LessonService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/rwe/lesson")
public class LessonController {

    private static final String PREFIX = "rwe/lesson";

    @Autowired
    private LessonService lesssonService;

    @GetMapping("")
    public String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/lesson")
    public String lessons(Model model) {
        List<LessonDO> lessons = lesssonService.list(new HashMap<>());
        model.addAttribute("lessons", lessons);
        return PREFIX + "/lesson";
    }

    @GetMapping("/add")
    @RequiresPermissions("rwe:lesson:add")
    public String add() {
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("rwe:lesson:add")
    public R save(LessonDO lesson) {

        try {
            int save = lesssonService.save(lesson);
            if (save != 1) {
                return R.error("新增课程失败");
            }
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }

}
