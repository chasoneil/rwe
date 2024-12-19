package com.chason.rwe.service;

import com.chason.rwe.domain.TradeDO;

import java.util.List;
import java.util.Map;

public interface TradeService {

    TradeDO get(String orderId);

    List<TradeDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(TradeDO tradeDO);

    int update(TradeDO tradeDO);

    int remove(String orderId);

    int batchRemove(String[] orderIds);

}
