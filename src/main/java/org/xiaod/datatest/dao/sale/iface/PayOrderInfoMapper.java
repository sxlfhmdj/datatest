package org.xiaod.datatest.dao.sale.iface;

import org.xiaod.datatest.dao.sale.mapper.PayOrderInfo;

public interface PayOrderInfoMapper {
    int deleteByPrimaryKey(String payOrderId);

    int insert(PayOrderInfo record);

    int insertSelective(PayOrderInfo record);

    PayOrderInfo selectByPrimaryKey(String payOrderId);

    int updateByPrimaryKeySelective(PayOrderInfo record);

    int updateByPrimaryKey(PayOrderInfo record);
}