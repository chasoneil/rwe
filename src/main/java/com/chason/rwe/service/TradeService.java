package com.chason.rwe.service;

import com.chason.rwe.domain.TradeDO;

import java.util.List;
import java.util.Map;

public interface TradeService {

    TradeDO get(String orderId);

    List<TradeDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(TradeDO tradeDO);

    int batchSave(List<TradeDO> tradeDOList);

    int update(TradeDO tradeDO);

    int remove(String orderId);

    int batchRemove(String[] orderIds);

    /**
     * 获取一年内的总花费
     * @param year
     * @return
     */
    Double getSpentByYear(String year);

    /**
     * 获取一年内的总收益
     * @param year
     * @return
     */
    Double getEarnByYear(String year);
}
