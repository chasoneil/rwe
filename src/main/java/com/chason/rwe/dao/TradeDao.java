package com.chason.rwe.dao;

import com.chason.rwe.domain.TradeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeDao {

    TradeDO get(String orderId);

    List<TradeDO> list(Map<String, Object> map);

    List<TradeDO> getTradeRange(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(TradeDO tradeDO);

    int batchSave(List<TradeDO> tradeDOList);

    int update(TradeDO tradeDO);

    int remove(String orderId);

    int batchRemove(String[] orderIds);
}
