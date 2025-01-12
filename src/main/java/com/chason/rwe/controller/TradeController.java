package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.dict.AccountDict;
import com.chason.common.utils.*;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.enums.TradePlatform;
import com.chason.rwe.page.TradePage;
import com.chason.rwe.service.ConsumeCategoryService;
import com.chason.rwe.service.TradeService;
import com.chason.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 交易信息
 * trade controller
 * @author Chason
 * @date 2024/12/19
 */

@Controller
@RequestMapping("/rwe/trade")
public class TradeController extends BaseController {

    private static final String PREFIX = "rwe/trade";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ConsumeCategoryService consumeCategoryService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public String index() {
        return PREFIX + "/index";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {

        // 获取当前的用户信息
        int roleLevel = roleService.getRoleLevel(getUserId());
        if (roleLevel == 20) { // 普通用户
            params.put("createUserId", getUserId());
        }

        params.putIfAbsent("offset", 0);
        params.putIfAbsent("limit", 10);

        if (!StringUtils.isEmpty(params.get("platform"))) {
            String transfer = TradePlatform.getNameByCode((String) params.get("platform"));
            params.put("platform", transfer);
        }

        if (!StringUtils.isEmpty(params.get("searchText"))) {
            String searchText = (String) params.get("searchText");
            params.put("tradeObj", searchText);
            params.put("product", searchText);
        }

        Query query = new Query(params);
        List<TradeDO> tradeLists = tradeService.list(query);
        int total = tradeService.count(query);
        List<TradePage> tradePageList = new ArrayList<>();
        for (TradeDO tradeDO : tradeLists) {
            TradePage tradePage = new TradePage();
            ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(tradeDO.getCategoryId());
            if (consumeCategoryDO != null) {
                tradePage.setCategoryName(consumeCategoryDO.getCategoryName());
                tradePage.setCategoryType(consumeCategoryDO.getCategoryType());
            } else {
                tradePage.setCategoryName("-");
                tradePage.setCategoryType("-");
            }
            BeanUtils.copyProperties(tradeDO, tradePage);
            tradePageList.add(tradePage);
        }
        return new PageUtils(tradePageList, total);
    }

    @GetMapping("/edit/{orderId}")
    String edit(@PathVariable("orderId") String orderId, Model model) {

        TradeDO tradeDO = tradeService.get(orderId);

        ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.get(tradeDO.getCategoryId());

        long userId = getUserId();
        HashSet<String> names = AccountDict.getInstance().getCategoryNameDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);

        TradePage tradePage = new TradePage();
        BeanUtils.copyProperties(tradeDO, tradePage);

        if (consumeCategoryDO != null) {
            tradePage.setCategoryName(consumeCategoryDO.getCategoryName());
            tradePage.setCategoryType(consumeCategoryDO.getCategoryType());
        }

        model.addAttribute("trade", tradePage);
        model.addAttribute("names", names);
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/types")
    public R getTypes(@RequestParam("categoryName") String categoryName) {
        List<String> typeList = consumeCategoryService.listTypes(categoryName);
        return R.ok().put("types", typeList);
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(@RequestParam("orderId") String orderId,
                    @RequestParam("categoryType") String categoryType) {
        try {
            TradeDO tradeDO = tradeService.get(orderId);
            if (tradeDO == null) {
                return R.error("账单信息不存在");
            }

            ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.getByType(categoryType);
            if (consumeCategoryDO == null) {
                return R.error("分类不存在");
            }

            tradeDO.setCategoryId(consumeCategoryDO.getId());
            tradeService.update(tradeDO);
        }
        catch (Exception e) {
            return R.error("修改账单失败：" + e.getMessage());
        }
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(String orderId) {
        return tradeService.remove(orderId) > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") String[] orderIds) {
        int row = tradeService.batchRemove(orderIds);
        return row > 0 ? R.ok("批量删除成功，共删除" + row + "条数据") : R.error();
    }

    @GetMapping("/split/{orderId}")
    String split(@PathVariable("orderId") String orderId, Model model) {
        TradeDO tradeDO = tradeService.get(orderId.trim());
        if (tradeDO == null) {
            throw new RuntimeException("账单信息不存在");
        }

        model.addAttribute("trade", tradeDO);
        return PREFIX + "/split";
    }

    @ResponseBody
    @PostMapping("/doSplit")
    public R doSplit(@RequestParam("orderId") String orderId,
                    @RequestParam("products[]") String[] products,
                    @RequestParam("amounts[]") String[] amounts,
                    @RequestParam("tradeComments[]") String[] tradeComments) {

        TradeDO tradeDO = tradeService.get(orderId.trim());
        if (tradeDO == null) {
            return R.error("账单信息不存在");
        }

        if(!checkAmount(tradeDO, amounts)) {
            return R.error("金额总和不等于账单金额");
        }

        boolean success = true;
        List<String> tmpOrderIds = new ArrayList<>();
        for (int i=0; i<products.length; i++) {
            TradeDO trade = new TradeDO();
            BeanUtils.copyProperties(tradeDO, trade);
            trade.setProduct(products[i].trim());
            trade.setAmount(Double.parseDouble(amounts[i].trim()));
            String tmpOrderId = UUID.randomUUID().toString().replaceAll("-", "");
            tmpOrderIds.add(tmpOrderId);
            trade.setOrderId(tmpOrderId);
            trade.setTradeComment(tradeComments[i].trim());
            if (tradeService.save(trade) == 0) {
                success = false;
                break;
            }
        }

        // rollback data
        if (!success) {
            for (String id : tmpOrderIds) {
                tradeService.remove(id);
            }
            return R.error("账单分割失败");
        }

        tradeService.remove(orderId);
        return R.ok("账单分割成功，原账单已删除");
    }

    private boolean checkAmount(TradeDO tradeDO, String[] amounts) {
        double totalAmount = 0;
        for (String amount : amounts) {
            totalAmount += Double.parseDouble(amount.trim());
        }
        return totalAmount == tradeDO.getAmount();
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
            return R.ok("暂不支持Excel导入！");
            //result = doExcelImport(file);
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

        String encoding = "UTF-8";
        if (file.getOriginalFilename().contains("alipay")) {
            encoding = "GBK";
        }

        TradePlatform dataSource = TradePlatform.UNKNOWN;
        boolean typeFound = false;
        if (file.getOriginalFilename().contains("alipay") || file.getOriginalFilename().contains("支付宝")) {
            dataSource = TradePlatform.ALIPAY;
            typeFound = true;
        } else if (file.getOriginalFilename().contains("wechat") || file.getOriginalFilename().contains("微信")) {
            dataSource = TradePlatform.WECHAT;
            typeFound = true;
        }

        HashSet<String> orderIds = new HashSet<>();
        List<TradeDO> list = tradeService.list(new HashMap<>());
        for (TradeDO trade : list) {
            orderIds.add(trade.getOrderId());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), encoding))) {

            List<TradeDO> trades = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(delimiter);
                // 解析账单类型
                if (columns.length == 1) {  // 综合数据
                    if (!typeFound) {
                        if (columns[0].contains("支付宝")) {
                            dataSource = TradePlatform.ALIPAY;
                            typeFound = true;
                        }

                        if (columns[0].contains("微信")) {
                            dataSource = TradePlatform.WECHAT;
                            typeFound = true;
                        }
                    }
                    continue;
                }

                if (columns.length > 1 ) {
                    // 解析数据 有效数据行
                    if (StringUtils.isNotNull(columns[1])) {
                        TradeDO tradeDO = null;
                        switch (dataSource) {
                            case ALIPAY:
                                // 解析支付宝账单
                                if ("金额".equals(columns[6]) || orderIds.contains(columns[9].trim())) {  // title
                                    continue;
                                }

                                tradeDO = doAlipayImport(columns);
                                trades.add(tradeDO);
                                break;
                            case WECHAT:
                                // 解析微信账单
                                if (columns[5].contains("金额") || orderIds.contains(columns[8].trim())) {
                                    continue;
                                }
                                tradeDO = doWechatImport(columns);
                                trades.add(tradeDO);
                                break;
                            default:
                                // 未知数据源
                                break;
                        }
                        rowCount++; // 增加导入的行数
                    } else {
                        // 信息行
                        if (!typeFound) {
                            if (columns[0].contains("支付宝")) {
                                dataSource = TradePlatform.ALIPAY;
                                typeFound = true;
                            }
                            if (columns[0].contains("微信")) {
                                dataSource = TradePlatform.WECHAT;
                                typeFound = true;
                            }
                        }
                        continue;
                    }
                }
            }

            rowCount = trades.size();
            if (rowCount > 0) {
                tradeService.batchSave(trades);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowCount;
    }

    // 解析并导入支付宝账单 .csv
    private TradeDO doAlipayImport(String[] columns) throws Exception {
        // 解析数据
        TradeDO tradeDO = new TradeDO();
        for (int i = 0; i < columns.length; i++) {
            try {
                tradeDO.setTradeTime(sdf.parse(columns[0]));
            } catch (ParseException e) {
                // try other format
                tradeDO.setTradeTime(sdf2.parse(columns[0]));
            }
            tradeDO.setTradeType(columns[1].trim());
            tradeDO.setTradeObj(columns[2].trim());
            tradeDO.setObjAccount(columns[3].trim());
            tradeDO.setProduct(columns[4].trim());
            tradeDO.setInOut(columns[5].trim());
            tradeDO.setAmount(Double.parseDouble(columns[6]));
            tradeDO.setPayType(columns[7]);
            tradeDO.setTradeStatus(columns[8]);
            tradeDO.setOrderId(columns[9].trim());
            tradeDO.setSellerOrderId(columns[10].trim());
            tradeDO.setPlatform("支付宝");
            tradeDO.setCreateUserId(getUserId());
            tradeDO.setChecked(0);
            if (columns.length == 12) {
                tradeDO.setTradeComment(columns[11]);
            }
        }

        return tradeDO;
    }

    private TradeDO doWechatImport(String[] columns) throws Exception {
        // 解析数据
        TradeDO tradeDO = new TradeDO();
        for (int i = 0; i < columns.length; i++) {
            try {
                tradeDO.setTradeTime(sdf.parse(columns[0]));
            } catch (ParseException e) {
                // try other format
                tradeDO.setTradeTime(sdf2.parse(columns[0]));
            }
            tradeDO.setTradeType(columns[1].trim());
            tradeDO.setTradeObj(columns[2].trim());
            tradeDO.setProduct(columns[3].trim());
            tradeDO.setInOut(columns[4].trim());
            String amount = columns[5].replaceAll("¥", "");
            System.out.println(amount);
            tradeDO.setAmount(Double.parseDouble(amount));
            tradeDO.setPayType(columns[6].trim());
            tradeDO.setTradeStatus(columns[7].trim());
            tradeDO.setOrderId(columns[8].trim());
            tradeDO.setSellerOrderId(columns[9].trim());
            tradeDO.setPlatform("微信");
            tradeDO.setCreateUserId(getUserId());
            tradeDO.setChecked(0);
            if (columns.length > 10) {
                tradeDO.setTradeComment(columns[10]);
            }
        }
        return tradeDO;
    }
}


