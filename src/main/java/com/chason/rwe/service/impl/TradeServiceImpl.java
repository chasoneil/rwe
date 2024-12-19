package com.chason.rwe.service.impl;

import com.chason.rwe.dao.TradeDao;
import com.chason.rwe.domain.TradeDO;
import com.chason.rwe.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
