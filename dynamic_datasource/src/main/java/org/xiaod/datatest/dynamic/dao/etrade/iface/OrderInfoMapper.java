package org.xiaod.datatest.dynamic.dao.etrade.iface;

import org.xiaod.datatest.dynamic.dao.etrade.mapper.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}