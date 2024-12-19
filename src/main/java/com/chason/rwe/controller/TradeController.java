package com.chason.rwe.controller;


import com.chason.common.utils.R;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * 交易信息
 * trade controller
 * @author Chason
 * @date 2024/12/19
 */

@Controller
@RequestMapping("/rwe/trade")
public class TradeController {

    private static final String PREFIX = "rwe/trade";

    @Autowired
    private TradeService tradeService;

    @ResponseBody
    @GetMapping("/list")
    public List<TradeDO> list() {
        return tradeService.list(new HashMap<>());
    }

    @ResponseBody
    @PostMapping("/import")
    public R importFile(@RequestParam("file")MultipartFile file) {

        if (!(file.getName().endsWith(".xls") || file.getName().endsWith("xlsx") ||
                file.getName().endsWith(".csv") || file.getName().endsWith(".txt"))) {
            return R.error("请上传Excel、CSV、TXT格式的文件！");
        }

        if (file.getSize() == 0) {
            return R.error("文件内容为空！");
        }

        int result = 0;

        if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
            result = doExcelImport(file);
        }

        if (file.getName().endsWith(".csv") || file.getName().endsWith(".txt")) {
            result = doCsvTxtImport(file);
        }

        return R.ok("导入成功，共导入" + result + "条数据！");
    }

    // 解析并导入Excel文件
    private int doExcelImport(MultipartFile file) {


        return 0;
    }

    // 解析并导入CSV/TXT文件
    private int doCsvTxtImport(MultipartFile file) {


        return 0;
    }

}


