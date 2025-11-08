package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 背单词
 */
@Controller
@RequestMapping("/rwe/jp/rem")
public class JpRemberController extends BaseController {

    private static final String PREFIX = "rwe/jp/rem";

    @Autowired
    private JpWordService jpWordService;

    @Autowired
    private JpLessonService jpLessonService;

    @GetMapping("")
    String index(Model model) {
        List<JpLessonDO> jpLessonDOS = jpLessonService.list(new HashMap<>());
        model.addAttribute("lessons", jpLessonDOS);
        return PREFIX + "/index";
    }

    @GetMapping("/rem/{id}")
    String rem(@PathVariable("id") Integer id, Model model) {
        JpLessonDO jpLessonDO = jpLessonService.get(id);

        Map<String, Object> param = new HashMap<>();
        param.put("lessonId", id);
        List<JpWordDO> jpWords = jpWordService.list(param);
        for (JpWordDO jpWord : jpWords) {
            if (jpWord.getLearned() == 2) {
                jpWords.remove(jpWord);
            }
        }

        model.addAttribute("jpWords", jpWords);
        model.addAttribute("jpLesson", jpLessonDO);
        return PREFIX + "/rem";
    }




}
