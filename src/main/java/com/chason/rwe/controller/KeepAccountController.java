package com.chason.rwe.controller;

import com.chason.common.controller.BaseController;
import com.chason.common.dict.AccountDict;
import com.chason.common.utils.PageUtils;
import com.chason.common.utils.Query;
import com.chason.common.utils.R;
import com.chason.common.utils.StringUtils;
import com.chason.rwe.domain.AccountDictDO;
import com.chason.rwe.domain.ConsumeCategoryDO;
import com.chason.rwe.domain.KeepAccountDO;
import com.chason.rwe.service.AccountDictService;
import com.chason.rwe.service.ConsumeCategoryService;
import com.chason.rwe.service.KeepAccountService;
import com.chason.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 记账信息
 * keep account controller
 * @author Chason
 * @date 2024/12/30
 */

@Controller
@RequestMapping("/rwe/keep_account")
public class KeepAccountController extends BaseController {

    private static final String PREFIX = "rwe/keep_account";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private KeepAccountService keepAccountService;

    @Autowired
    private ConsumeCategoryService consumeCategoryService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountDictService accountDictService;

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

        if (!StringUtils.isEmpty(params.get("searchText"))) {
            params.put("tradeComment", params.get("searchText"));
        }

        Query query = new Query(params);
        List<KeepAccountDO> keepAccountDOList = keepAccountService.list(query);
        int total = keepAccountService.count(query);
        return new PageUtils(keepAccountDOList, total);
    }

    @GetMapping("/add")
    String add(Model model) {

        long userId = getUserId();
        HashSet<String> types = AccountDict.getInstance().getCategoryTypeDict(consumeCategoryService,
                roleService.getRoleLevel(userId), userId).get(userId);

        Map<String, Object> params = new HashMap<>();
        params.put("dictName", "消费人");
        List<AccountDictDO> consumers = accountDictService.list(params);

        model.addAttribute("types", types);
        model.addAttribute("consumers", consumers);
        return PREFIX + "/add";
    }

    @ResponseBody
    @PostMapping("/save")
    public R save(KeepAccountDO keepAccountDO) {
        try {
            check(keepAccountDO);
            ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.getByType(keepAccountDO.getTradeType());
            if (consumeCategoryDO == null) {
                throw new RuntimeException("未定义的分类");
            }

//            if (!keepAccountService.checkKeepAccount(keepAccountDO)) {
//                throw new RuntimeException("记账信息重复");
//            }

            keepAccountDO.setTradeVariety(consumeCategoryDO.getCategoryName());
            keepAccountDO.setTradeType(consumeCategoryDO.getCategoryType());
            keepAccountDO.setTradePeriod(consumeCategoryDO.getBillType());
            keepAccountDO.setTradeStatistics(consumeCategoryDO.getDeepType());
            keepAccountDO.setUserId(getUserId());
            keepAccountDO.setTradeStatus("交易成功");
            int save = keepAccountService.save(keepAccountDO);
            if (save != 1) {
                return R.error("记账失败");
            }
        } catch (Exception e) {
            return R.error("记账失败：" + e.getMessage());
        }
        return R.ok();
    }

    @ResponseBody
    @PostMapping("/inout")
    public R getCategory(@RequestParam("inOut") String inOut) {
        HashSet<String> names = consumeCategoryService.listNames(inOut);
        return R.ok().put("names", names);
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") int id, Model model) {
        KeepAccountDO keepAccountDO = keepAccountService.get(id);
        if (keepAccountDO == null) {
            throw new RuntimeException("记账信息不存在");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("dictName", "消费人");
        List<AccountDictDO> consumers = accountDictService.list(params);

        model.addAttribute("keepAccount", keepAccountDO);
        model.addAttribute("consumers", consumers);
        return PREFIX + "/edit";
    }

    @ResponseBody
    @PostMapping("/update")
    public R update(KeepAccountDO keepAccountDO) {
        try {
            check(keepAccountDO);
            ConsumeCategoryDO consumeCategoryDO = consumeCategoryService.getByType(keepAccountDO.getTradeType());
            if (consumeCategoryDO == null) {
                throw new RuntimeException("未定义的分类");
            }

            keepAccountDO.setTradeVariety(consumeCategoryDO.getCategoryName());
            keepAccountDO.setTradeType(consumeCategoryDO.getCategoryType());
            keepAccountDO.setTradePeriod(consumeCategoryDO.getBillType());
            keepAccountDO.setTradeStatistics(consumeCategoryDO.getDeepType());
            int update = keepAccountService.update(keepAccountDO);
            if (update != 1) {
                return R.error("修改记账信息失败");
            }
        }
        catch (Exception e) {
            return R.error("修改记账信息失败：" + e.getMessage());
        }
        return R.ok();
    }

    @PostMapping("/remove")
    @ResponseBody
    public R remove(Integer id) {
        return keepAccountService.remove(id) > 0 ? R.ok("删除成功") : R.error("删除失败");
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") int[] ids) {
        int row = keepAccountService.batchRemove(ids);
        return row > 0 ? R.ok("批量删除成功，共删除" + row + "条数据") : R.error();
    }


    private void check(KeepAccountDO keepAccountDO) {
        if (!StringUtils.isNotNull(keepAccountDO.getTradeTime())) {
            throw new RuntimeException("交易时间不能为空");
        }

        if (!StringUtils.isNotNull(keepAccountDO.getTradeType())) {
            throw new RuntimeException("交易类型不能为空");
        }

        if (keepAccountDO.getAmount() == 0) {
            throw new RuntimeException("交易金额不能为零或者为空");
        }

        if (!StringUtils.isNotNull(keepAccountDO.getPayFor())) {
            throw new RuntimeException("消费者不能为空");
        }
    }
}


