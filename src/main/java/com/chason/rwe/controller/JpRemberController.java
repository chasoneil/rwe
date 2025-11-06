package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


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
    String index() {
        return PREFIX + "/index";
    }




}
