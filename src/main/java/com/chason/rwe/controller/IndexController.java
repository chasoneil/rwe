package com.chason.rwe.controller;

import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.KeepAccountDO;
import com.chason.rwe.page.TradePiePage;
import com.chason.rwe.service.KeepAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/rwe/index")
public class IndexController {

    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private KeepAccountService keepAccountService;

    @ResponseBody
    @PostMapping("/pie/out")
    public R initPieOut(@RequestParam("date") String date) {

        Map<String, Object> data = new HashMap<>();
        List<TradePiePage> piePages = new ArrayList<>();
        if (!StringUtils.isNotNull(date)) {  // 初始化本月的数据
            String month = MONTH_FORMAT.format(new Date());
            List<KeepAccountDO> keepAccountDOList = keepAccountService.listMonthSpent(month);
            Map<String, Double> statisticMap = new HashMap<>();
            for (KeepAccountDO keepAccountDO : keepAccountDOList) {
                statisticMap.put(keepAccountDO.getTradeVariety(),
                        statisticMap.getOrDefault(keepAccountDO.getTradeVariety(), 0.0) + keepAccountDO.getAmount());
            }

            for (String key :statisticMap.keySet()) {
                TradePiePage piePage = new TradePiePage();
                piePage.setValue(statisticMap.get(key));
                piePage.setName(key);
                piePages.add(piePage);
            }

            data.put("data", piePages);
        } else {    // 根据日期查询数据并返回

        }
        return R.ok(data);
    }

    @ResponseBody
    @PostMapping("/statistic")
    public R initStatistic(@RequestParam("year") String year,
                           @RequestParam("month") String month) {
        double monthSpent = 0.0;
        double monthIncome = 0.0;

        double yearSpent = 0.0;
        double yearIncome = 0.0;

        List<KeepAccountDO> monthSpentList = keepAccountService.listMonthSpent(year + "-" + month);
        for (KeepAccountDO keepAccountDO : monthSpentList) {
            monthSpent += keepAccountDO.getAmount();
        }

        List<KeepAccountDO> monthIncomeList = keepAccountService.listMonthIncome(year + "-" + month);
        for (KeepAccountDO keepAccountDO : monthIncomeList) {
            monthIncome += keepAccountDO.getAmount();
        }

        List<KeepAccountDO> yearSpentList = keepAccountService.listYearSpent(year);
        for (KeepAccountDO keepAccountDO : yearSpentList) {
            yearSpent += keepAccountDO.getAmount();
        }

        List<KeepAccountDO> yearIncomeList = keepAccountService.listYearIncome(year);
        for (KeepAccountDO keepAccountDO : yearIncomeList) {
            yearIncome += keepAccountDO.getAmount();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("monthSpent", monthSpent);
        data.put("monthIncome", monthIncome);
        data.put("yearSpent", yearSpent);
        data.put("yearIncome", yearIncome);

        return R.ok(data);
    }


    @ResponseBody
    @PostMapping("/pie/in")
    public R initPieIn(@RequestParam("date") String date) {

        Map<String, Object> data = new HashMap<>();
        List<TradePiePage> piePages = new ArrayList<>();
        if (!StringUtils.isNotNull(date)) {  // 初始化本月的数据
            String month = MONTH_FORMAT.format(new Date());
            List<KeepAccountDO> keepAccountDOList = keepAccountService.listMonthIncome(month);
            Map<String, Double> statisticMap = new HashMap<>();
            for (KeepAccountDO keepAccountDO : keepAccountDOList) {
                statisticMap.put(keepAccountDO.getTradeVariety(),
                        statisticMap.getOrDefault(keepAccountDO.getTradeVariety(), 0.0) + keepAccountDO.getAmount());
            }

            for (String key :statisticMap.keySet()) {
                TradePiePage piePage = new TradePiePage();
                piePage.setValue(statisticMap.get(key));
                piePage.setName(key);
                piePages.add(piePage);
            }

            data.put("data", piePages);
        } else {    // 根据日期查询数据并返回

        }
        return R.ok(data);
    }

    @ResponseBody
    @PostMapping("/calendar")
    public R initCalendar(@RequestParam("date") String date) {

        Map<String, Object> params = new HashMap<>();
        double income = 0.0;
        double spent = 0.0;

        if (StringUtils.isNotNull(date)) {

            params.put("tradeTime", date);
            List<KeepAccountDO> list = keepAccountService.list(params);
            for (KeepAccountDO keepAccountDO : list) {
                if (keepAccountDO.getInOut().equals("收入")) {
                    income += keepAccountDO.getAmount();
                } else if (keepAccountDO.getInOut().equals("支出")) {
                    spent += keepAccountDO.getAmount();
                }
            }
        }
        return R.ok().put("income", income).put("spent", spent);
    }



}
