package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.common.utils.R;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rwe/jp/word")
public class JpWordsController extends BaseController {

    private static final String PREFIX = "rwe/jp/word";

    @Autowired
    private JpWordService jpWordService;

    @Autowired
    private JpLessonService jpLessonService;

    @GetMapping("/index")
    @RequiresPermissions("rwe:word")
    String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/add/{id}")
    String add(@PathVariable("id") Integer id, Model model) {
        JpLessonDO jpLessonDO = jpLessonService.get(id);
        model.addAttribute("lesson", jpLessonDO);
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    R save(JpWordDO jpWordDO) {
        try {
            jpWordService.save(jpWordDO);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }


}
