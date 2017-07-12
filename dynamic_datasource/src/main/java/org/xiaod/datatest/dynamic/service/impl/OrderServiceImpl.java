package org.xiaod.datatest.dynamic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xiaod.datatest.dynamic.common.datasource.DatabaseContextHolder;
import org.xiaod.datatest.dynamic.common.datasource.DatabaseType;
import org.xiaod.datatest.dynamic.dao.etrade.iface.OrderInfoMapper;
import org.xiaod.datatest.dynamic.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.dynamic.service.OrderService;

/**
 * Description: 【方法功能中文描述】 <br/>
 * Created on 23:41 2017/7/11 <br/>
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Transactional("dynamicTransactionManager")
    public void addOrder(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId("20170710121208002");
        orderInfo.setOrderType("1");
        OrderInfo orderInfo1 = new OrderInfo();
        orderInfo1.setOrderId("20170710121208001");
        orderInfo.setOrderType("1");
        DatabaseContextHolder.setDatabaseType(DatabaseType.etrade);
        orderInfoMapper.insert(orderInfo);
        orderInfoMapper.insert(orderInfo1);
    }
}
