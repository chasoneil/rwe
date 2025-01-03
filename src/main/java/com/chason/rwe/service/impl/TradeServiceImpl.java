package com.chason.rwe.service.impl;

import com.chason.rwe.dao.TradeDao;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeDao tradeDao;

    @Override
    public TradeDO get(String orderId) {
        return tradeDao.get(orderId);
    }

    @Override
    public List<TradeDO> list(Map<String, Object> map) {
        return tradeDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return tradeDao.count(map);
    }

    @Override
    public int save(TradeDO tradeDO) {
        return tradeDao.save(tradeDO);
    }

    @Override
    public int batchSave(List<TradeDO> tradeDOList) {
        return tradeDao.batchSave(tradeDOList);
    }

    @Override
    public int update(TradeDO tradeDO) {
        return tradeDao.update(tradeDO);
    }

    @Override
    public int remove(String orderId) {
        return tradeDao.remove(orderId);
    }

    @Override
    public int batchRemove(String[] orderIds) {
        return tradeDao.batchRemove(orderIds);
    }

    /**
     * 计算一年的支出情况
     * @param year 年份
     * @return 当年的支出
     */
    @Override
    public Double getYearSpent(String year) {

        double sum = 0.0;
        String start = year + "-01-01";
        String end = year + "-12-31";
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", start);
        map.put("endTime", end);
        List<TradeDO> trades = tradeDao.getTradeRange(map);

        if (trades == null || trades.isEmpty()) {
            return sum;
        }

        for (TradeDO trade : trades) {
            if (trade.getInOut().equals("支出")) {
                sum += trade.getAmount();
            }
        }
        return sum;
    }

    /**
     * 计算一年的收入情况
     * @param year 年份
     * @return 当年的收入
     */
    @Override
    public Double getYearEarned(String year) {

        double sum = 0.0;
        String start = year + "-01-01";
        String end = year + "-12-31";
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", start);
        map.put("endTime", end);
        List<TradeDO> trades = tradeDao.getTradeRange(map);

        if (trades == null || trades.isEmpty()) {
            return sum;
        }

        for (TradeDO trade : trades) {
            if (trade.getInOut().equals("收入")) {
                sum += trade.getAmount();
            }
        }
        return sum;
    }

    @Override
    public Double getMonthSpent(String year, String month) {

        double sum = 0.0;
        String start = year + "-" + month + "-01";
        String end = year + "-" + month + "-31";
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", start);
        map.put("endTime", end);
        List<TradeDO> trades = tradeDao.getTradeRange(map);

        if (trades == null || trades.isEmpty()) {
            return sum;
        }

        for (TradeDO trade : trades) {
            if (trade.getInOut().equals("支出")) {
                sum += trade.getAmount();
            }
        }

        return sum;
    }

    @Override
    public Double getMonthEarned(String year, String month) {
        double sum = 0.0;
        String start = year + "-" + month + "-01";
        String end = year + "-" + month + "-31";
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", start);
        map.put("endTime", end);
        List<TradeDO> trades = tradeDao.getTradeRange(map);

        if (trades == null || trades.isEmpty()) {
            return sum;
        }

        for (TradeDO trade : trades) {
            if (trade.getInOut().equals("收入")) {
                sum += trade.getAmount();
            }
        }
        return sum;
    }
}
