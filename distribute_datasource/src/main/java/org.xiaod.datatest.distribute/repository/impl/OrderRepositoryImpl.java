package org.xiaod.datatest.distribute.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xiaod.datatest.distribute.dao.etrade.iface.OrderInfoMapper;
import org.xiaod.datatest.distribute.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.distribute.repository.OrderRepository;

/**
 * Description: 【订单仓储实现】 <br/>
 * Created on 18:33 2017/7/10 <br/>
 *
 * Transaction事务注解最好使用使用在类和实现方法上，不要使用在接口和接口方法上。
 * 只有对接口代理的时候，使用在接口和接口方法上的Transaction才生效
 * Transaction事务注解只是针对类中的public方法生效，对于private和protected即使使用了也不会生效
 * Transaction事务注解类中方法之间的调用事务不会起作用，只是对外部的调用才会起作用
 *
 * 使用Transaction时，要启用Transaction即 @EnableTransactionManagement注解在Application上
 * 并且操作的数据库要支持事务回滚，即数据库引擎是innoDB
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public void addOrder(OrderInfo order){
        orderInfoMapper.insert(order);
    }

    public void updateOrderByID(OrderInfo order){
        orderInfoMapper.updateByPrimaryKey(order);
    }

    public void deleteOrder(String orderID){
        orderInfoMapper.deleteByPrimaryKey(orderID);
    }
}
