package org.xiaod.datatest.multi.repository;

import org.xiaod.datatest.multi.dao.etrade.mapper.OrderInfo;

/**
 * Description: 【订单仓储接口】 <br/>
 * Created on 18:33 2017/7/10 <br/>
 *
 */
public interface OrderRepository {
    /**
     * 添加订单
     *
     * @param order
     */
    public void addOrder(OrderInfo order);

    /**
     * 更新订单
     *
     * @param order
     */
    public void updateOrderByID(OrderInfo order);

    /**
     * 删除订单
     *
     * @param orderID
     */
    public void deleteOrder(String orderID);
}
