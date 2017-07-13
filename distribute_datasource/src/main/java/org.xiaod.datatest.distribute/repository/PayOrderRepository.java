package org.xiaod.datatest.distribute.repository;

import org.xiaod.datatest.distribute.dao.sale.mapper.PayOrderInfo;

/**
 * Description: 【支付订单仓储接口】 <br/>
 * Created on 18:32 2017/7/10 <br/>
 *
 */
public interface PayOrderRepository {

    public void addOrder(PayOrderInfo order);

    public void updateOrderByID(PayOrderInfo order);

    public void deleteOrder(String payOrderID);
}
