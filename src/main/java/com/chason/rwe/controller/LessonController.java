package com.chason.rwe.controller;

import com.chason.rwe.domain.LessonDO;
import com.chason.rwe.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return PREFIX + "/lesson";
    }

}
