package org.xiaod.datatest.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaod.datatest.dao.etrade.iface.OrderInfoMapper;
import org.xiaod.datatest.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.dao.sale.iface.PayOrderInfoMapper;
import org.xiaod.datatest.dao.sale.mapper.PayOrderInfo;

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

    @Test
    public void test(){
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey("20170710121208001");
        System.out.println(orderInfo.toString());
        PayOrderInfo payOrderInfo = payOrderInfoMapper.selectByPrimaryKey("1020170710122019001");
        System.out.println(payOrderInfo.toString());
    }
}
