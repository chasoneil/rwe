package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rwe/jp/word")
public class JpWordsController extends BaseController {

    private static final String PREFIX = "rwe/jp/word";

    @Autowired
    private JpWordService jpWordService;

    @Autowired
    private JpLessonService jpLessonService;

    @GetMapping("")
    String index() {
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    PageUtils list(@RequestParam Map<String, Object> params) {

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        Query query = new Query(params);
        List<JpWordDO> words = jpWordService.list(query);
        int total = jpWordService.count(query);
        return new PageUtils(words, total);
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

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        JpWordDO jpWordDO = jpWordService.get(id);
        JpLessonDO jpLessonDO = jpLessonService.get(jpWordDO.getLessonId());
        model.addAttribute("jpWord", jpWordDO);
        model.addAttribute("lesson", jpLessonDO.getLesson());
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    R update(JpWordDO jpWordDO) {

        try {
            jpWordService.update(jpWordDO);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/remove")
    R remove(Integer id) {
        try {
            jpWordService.remove(id);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
