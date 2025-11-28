package com.chason.rwe.controller;

import com.alibaba.fastjson.JSON;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.DialogDO;
import com.chason.rwe.domain.JpSentenceDO;
import com.chason.rwe.domain.SingleDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/rwe/jp/sentence")
public class JpSentenceController {

    private static final String PREFIX = "rwe/jp/sentence";

    private static List<String> cache = new ArrayList<>();

    @GetMapping("")
    String index(Model model) {
        // 获取所有的练习文件名
        List<String> lessons = new ArrayList<>();

        // 获取 resource 目录
        String notePath = JpSentenceController.class.getResource("/").getPath() + "/notes";
        File dir = new File(notePath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f: files) {
                    lessons.add(removeSuffix(f.getName()));
                }
            }
        }

        model.addAttribute("lessons", lessons);
        return PREFIX + "/index";
    }

    @GetMapping("/practice/${lessonName}")
    String practice(@PathVariable("lessonName") String lessonName) {

    }


    @PostMapping("/data")
    @ResponseBody
    R getData (@RequestParam String lessonName) {

        if (StringUtils.isEmpty(lessonName)) {
            log.warn("get lesson error {}", lessonName);
            return R.error("没找到该课程");
        }

        String fileName = lessonName + ".txt";
        String filePath = JpSentenceController.class.getResource("/").getPath() + "/notes" + "/" + fileName;

        try {
            JpSentenceDO jpSentenceDO = init(filePath);
            String res = JSON.toJSONString(jpSentenceDO);
            return R.ok(res);
        } catch (Exception e) {
            log.error("practice error:{}", e.getMessage());
            return R.error("获取练习数据失败");
        }
    }

    private JpSentenceDO init(String filePath) {
        JpSentenceDO jpSentenceDO = new JpSentenceDO();
        String key = null;
        int type = 0;
        try {
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (StringUtils.isEmpty(line)) {
                    if (!cache.isEmpty()) {
                        flushDialog(key, jpSentenceDO);
                    }
                    continue;
                }

                if (line.startsWith("T")) {
                    key = line.substring(2);
                    type = 0;
                    continue;
                }

                if (line.startsWith("单句") ) {
                    type = 0;
                    continue;
                }

                if (line.startsWith("对话")) {
                    type = 1;
                    continue;
                }

                if (type == 0) {
                    initSingle(key, line, jpSentenceDO);
                    continue;
                }

                // 能到这里说明 当前肯定是对话且不为空行
                cache.add(line);
            }
        } catch (IOException e) {
            log.error("init sentence error : {}", e.getMessage());
            throw new RuntimeException("初始化数据异常");
        }

        return jpSentenceDO;
    }

    private  void initSingle(String key, String content, JpSentenceDO jpSentence) {
        SingleDO single = new SingleDO(key, content);
        jpSentence.getSingles().add(single);
    }


    private void flushDialog(String key, JpSentenceDO jpSentence) {
        DialogDO dialog = new DialogDO(key);
        List<String> content = new ArrayList<>(cache);
        dialog.setContent(content);
        jpSentence.getDialogs().add(dialog);
        cache.clear();
    }

    private String removeSuffix(String fileName) {

        if (StringUtils.isEmpty(fileName)) {
            return fileName;
        }

        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex != -1 && dotIndex != 0) {
            return fileName.substring(0, dotIndex);
        } else {
            return fileName;
        }
    }

}
