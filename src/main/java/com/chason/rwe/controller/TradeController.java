package com.chason.rwe.controller;


import com.chason.common.domain.TaskDO;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.enums.TradeDataSourceEnum;
import com.chason.rwe.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private TradeService tradeService;

    @GetMapping("/index")
    public String index() {
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<TradeDO> tradeLists = tradeService.list(query);
        int total = tradeService.count(query);
        return new PageUtils(tradeLists, total);
    }

    @GetMapping("/import")
    public String importFile() {
        return PREFIX + "/import";
    }

    @ResponseBody
    @PostMapping("/uploadFile")
    public R doImportFile(@RequestParam("file") MultipartFile file) {

        if (!(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith("XLS") ||
                file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".XLSX") ||
                file.getOriginalFilename().endsWith(".csv") || file.getOriginalFilename().endsWith(".CSV") ||
                file.getOriginalFilename().endsWith(".txt") || file.getOriginalFilename().endsWith(".TXT"))) {
            return R.error("请上传Excel、CSV、TXT格式的文件！");
        }

        if (file.getSize() == 0) {
            return R.error("文件内容为空！");
        }

        int result = 0;

        if (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx")
        || file.getOriginalFilename().endsWith(".XLSX") || file.getOriginalFilename().endsWith(".XLS")) {
            result = doExcelImport(file);
        }

        if (file.getOriginalFilename().endsWith(".csv") || file.getOriginalFilename().endsWith(".txt") ||
        file.getOriginalFilename().endsWith(".TXT") || file.getOriginalFilename().endsWith(".CSV")) {
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

        String delimiter = ",";
        int rowCount = 0; // 用于记录导入的行数
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "GBK"))) {
            String line;

            TradeDataSourceEnum dataSource = TradeDataSourceEnum.UNKNOWN;

            boolean typeFound = false;
            List<TradeDO> trades = new ArrayList<>();
            while ((line = br.readLine()) != null) {

                String[] columns = line.split(delimiter);

                // 解析账单类型
                if (columns.length == 1) {
                    if (!typeFound) {

                        if (columns[0].contains("支付宝")) {
                            dataSource = TradeDataSourceEnum.ALIPAY;
                            typeFound = true;
                        }

                        if (columns[0].contains("微信")) {
                            dataSource = TradeDataSourceEnum.WECHAT;
                            typeFound = true;
                        }
                    }
                    continue;
                }
                switch (dataSource) {
                    case ALIPAY:
                        // 解析支付宝账单
                        if (rowCount != 0) {
                            TradeDO tradeDO = doAlipayImport(columns);
                            trades.add(tradeDO);
                        }
                        break;
                    case WECHAT:
                        // 解析微信账单
                        break;
                    default:
                        // 未知数据源
                        break;
                }
                rowCount++; // 增加导入的行数
            }

            rowCount = trades.size();
            if (rowCount > 0) {
                tradeService.batchSave(trades);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    // 解析并导入支付宝账单 .csv
    private TradeDO doAlipayImport(String[] columns) throws ParseException {
        // 解析数据
        TradeDO tradeDO = new TradeDO();
        for (int i = 0; i < columns.length; i++) {
            tradeDO.setTradeTime(sdf.parse(columns[0]));
            tradeDO.setTradeType(columns[1]);
            tradeDO.setTradeObj(columns[2]);
            tradeDO.setObjAccount(columns[3]);
            tradeDO.setProduct(columns[4]);
            tradeDO.setInOut(columns[5]);
            tradeDO.setAmount(Double.parseDouble(columns[6]));
            tradeDO.setPayType(columns[7]);
            tradeDO.setTradeStatus(columns[8]);
            tradeDO.setOrderId(columns[9]);
            tradeDO.setSellerOrderId(columns[10]);
            if (columns.length == 12) {
                tradeDO.setTradeComment(columns[11]);
            }
        }

        return tradeDO;
    }
}


