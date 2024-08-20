package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.common.utils.R;
import com.chason.rwe.domain.WordDO;
import com.chason.rwe.service.WordService;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rwe/word")
public class WordsController extends BaseController {

    private static final String PREFIX = "rwe/word";

    @Autowired
    private WordService wordService;

    @GetMapping("/index")
    @RequiresPermissions("rwe:word")
    public String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/add/{lessonId}")
    public String add(@PathVariable("lessonId") String lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(WordDO word) {

        try {
            if (wordService.save(word) > 0) {
                return R.ok();
            }
        } catch (Exception e) {
            return R.error(e.getMessage());
        }

        return R.error();
    }


}
