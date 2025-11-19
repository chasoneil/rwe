package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;

import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.JpLessonDO;
import com.chason.rwe.domain.JpWordDO;
import com.chason.rwe.enums.WordTypeEnum;
import com.chason.rwe.service.JpLessonService;
import com.chason.rwe.service.JpWordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@RequestMapping("/rwe/jp/word")
public class JpWordsController extends BaseController {

    private static final String PREFIX = "rwe/jp/word";

    @Autowired
    private JpWordService jpWordService;

    @Autowired
    private JpLessonService jpLessonService;

    @GetMapping("")
    String index(Model model) {
        Map<String, Object> param = new HashMap<>();
        Long userId = getUserId();
        param.put("userId", userId);
        List<JpLessonDO> jpLessonDOS = jpLessonService.list(param);
        model.addAttribute("lessons", jpLessonDOS);
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    PageUtils list(@RequestParam Map<String, Object> params) {

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        List<JpWordDO> words = new ArrayList<>();
        int total = 0;

        int lessonId = Integer.parseInt((String) params.get("lessonId"));
        if (lessonId == -1) {
            params.remove("lessonId");
        } else {
            JpLessonDO jpLessonDO = jpLessonService.get(lessonId);

            // 选择的课程不属于当前用户
            if (jpLessonDO != null &&
                    jpLessonDO.getUserId() != 1 && jpLessonDO.getUserId() != getUserId()) {
                return new PageUtils(words, total);
            }
        }

        Query query = new Query(params);
        words = jpWordService.list(query);
        for (JpWordDO w : words) {
            if (!StringUtils.isEmpty(w.getWordType())) {
                String t = w.getWordType();
                w.setWordType(WordTypeEnum.getNameFromSign(t));
            }
        }

        total = jpWordService.count(query);
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

    @GetMapping("/import/{id}")
    String importPage(@PathVariable("id") Integer id, Model model) {
        JpLessonDO jpLessonDO = jpLessonService.get(id);
        model.addAttribute("lesson", jpLessonDO);
        return PREFIX + "/import";
    }

    @ResponseBody
    @PostMapping("/import/word")
    public R doImport(@RequestParam("file") MultipartFile file, @RequestParam("lessonId") Integer lessonId) {


        if (!(file.getOriginalFilename().endsWith(".txt") ||  file.getOriginalFilename().endsWith(".TXT"))) {
            return R.error("请上传TXT格式的文件！");
        }

        if (file.getSize() == 0) {
            return R.error("文件内容为空！");
        }

        if (lessonId == -1) {
            return R.error("请选择课程后再执行导入！");
        }

        int count = doWordImport(file, lessonId);
        return R.ok("导入单词成功，本次导入" + count + "个单词");
    }

    // 假名 - 单词 - 含义 - 音型 - 词型
    private int doWordImport(MultipartFile file, Integer lessonId) {

        String delimiter = "-";
        int rowCount = 0;
        String encoding = "UTF-8";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), encoding))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (StringUtils.isEmpty(line)) {
                    continue;
                }

                String[] columns = line.split(delimiter);
                if (jpWordService.checkExist(columns[0], columns[3])) {
                    continue;
                }
                JpWordDO jpWordDO = new JpWordDO();
                jpWordDO.setWord(columns[0]);
                if (StringUtils.isEmpty(columns[1])) {
                    jpWordDO.setWordCn(columns[0]);
                } else {
                    jpWordDO.setWordCn(columns[1]);
                }
                jpWordDO.setWordCn(columns[1]);
                jpWordDO.setWordVoice(columns[3]);
                jpWordDO.setZhMean(columns[2]);
                if (columns.length == 5 && !StringUtils.isEmpty(columns[4])) {
                    jpWordDO.setWordType(columns[4].toLowerCase());
                }
                jpWordDO.setLessonId(lessonId);
                jpWordDO.setLearnTime(0);
                jpWordDO.setLearned(0);
                jpWordDO.setCreateTime(new Date());

                jpWordService.save(jpWordDO);
                rowCount++;
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return rowCount;
    }
}
