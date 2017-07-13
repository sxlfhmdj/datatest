package org.xiaod.datatest.distribute.dao.etrade.iface;

import org.xiaod.datatest.distribute.dao.etrade.mapper.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}