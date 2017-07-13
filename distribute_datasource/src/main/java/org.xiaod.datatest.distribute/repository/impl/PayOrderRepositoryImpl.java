package org.xiaod.datatest.distribute.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xiaod.datatest.distribute.dao.sale.iface.PayOrderInfoMapper;
import org.xiaod.datatest.distribute.dao.sale.mapper.PayOrderInfo;
import org.xiaod.datatest.distribute.repository.PayOrderRepository;

/**
 * Description: 【支付订单仓储实现】 <br/>
 * Created on 18:32 2017/7/10 <br/>
 *
 */
@Repository
public class PayOrderRepositoryImpl implements PayOrderRepository{
    @Autowired
    private PayOrderInfoMapper payOrderInfoMapper;

    public void addOrder(PayOrderInfo order){
        payOrderInfoMapper.insert(order);
    }

    public void updateOrderByID(PayOrderInfo order){
        payOrderInfoMapper.updateByPrimaryKey(order);
    }

    public void deleteOrder(String payOrderID){
        payOrderInfoMapper.deleteByPrimaryKey(payOrderID);
    }
}
