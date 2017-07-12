package org.xiaod.datatest.multi.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaod.datatest.multi.dao.etrade.iface.OrderInfoMapper;
import org.xiaod.datatest.multi.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.multi.dao.sale.iface.PayOrderInfoMapper;
import org.xiaod.datatest.multi.dao.sale.mapper.PayOrderInfo;
import org.xiaod.datatest.multi.repository.OrderRepository;
import org.xiaod.datatest.multi.service.OrderService;

/**
 * Description: 【测试】 <br/>
 * Created on 9:27 2017/7/11 <br/>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DataTransactionTest {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private PayOrderInfoMapper payOrderInfoMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void test(){
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey("20170710121208001");
        System.out.println(orderInfo.toString());
        PayOrderInfo payOrderInfo = payOrderInfoMapper.selectByPrimaryKey("1020170710122019001");
        System.out.println(payOrderInfo.toString());
    }

    @Test

    public void testTransaction(){
        orderService.addOrder();
    }
}
