package org.xiaod.datatest.distribute.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaod.datatest.distribute.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.distribute.dao.sale.mapper.PayOrderInfo;
import org.xiaod.datatest.distribute.repository.OrderRepository;
import org.xiaod.datatest.distribute.repository.PayOrderRepository;
import org.xiaod.datatest.distribute.service.OrderService;

/**
 * Description: 【方法功能中文描述】 <br/>
 * Created on 23:41 2017/7/11 <br/>
 *
 */
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PayOrderRepository payOrderRepository;

    @Transactional
    public void addOrder(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId("20170710121208001");
        orderInfo.setOrderType("1");
        PayOrderInfo payOrderInfo = new PayOrderInfo();
        payOrderInfo.setPayOrderId("1020170710121208002");
        payOrderInfo.setOrderId("20170710121208002");
        payOrderInfo.setOrderState("1");
        payOrderRepository.addOrder(payOrderInfo);
        orderRepository.addOrder(orderInfo);
    }
}
