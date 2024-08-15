package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rwe/word")
public class WordsController extends BaseController {

    private static final String PREFIX = "rwe/word";

    @GetMapping("/index")
    @RequiresPermissions("rwe:word")
    public String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/classes")
    public String classes(Model model) {

        return "";
    }


}
