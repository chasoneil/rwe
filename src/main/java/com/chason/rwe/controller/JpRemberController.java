package com.chason.rwe.controller;

import com.alibaba.fastjson.JSON;
import com.chason.common.controller.BaseController;

import com.chason.common.utils.R;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        model.addAttribute("lessonId", id);
        return PREFIX + "/rem";
    }

    @ResponseBody
    @PostMapping("/rem/load/words")
    R doRem(@RequestParam("lessonId") Integer lessonId) {
        JpLessonDO jpLessonDO = jpLessonService.get(lessonId);

        Map<String, Object> param = new HashMap<>();
        param.put("lessonId", lessonId);
        List<JpWordDO> jpWords = jpWordService.list(param);
        for (JpWordDO jpWord : jpWords) {
            if (jpWord.getLearned() == 2) {
                jpWords.remove(jpWord);
            }
        }
        String res = JSON.toJSONString(jpWords);
        return R.ok(res);
    }

    @ResponseBody
    @PostMapping("/learn")
    R learn(@RequestParam("data") String data) {
        try {
            jpWordService.updateRem(data);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }
}
