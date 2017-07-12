package org.xiaod.datatest.dynamic.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaod.datatest.dynamic.common.datasource.DatabaseContextHolder;
import org.xiaod.datatest.dynamic.common.datasource.DatabaseType;
import org.xiaod.datatest.dynamic.dao.etrade.iface.OrderInfoMapper;
import org.xiaod.datatest.dynamic.dao.etrade.mapper.OrderInfo;
import org.xiaod.datatest.dynamic.service.OrderService;

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
    private OrderService orderService;

    @Test
    public void test(){
        DatabaseContextHolder.setDatabaseType(DatabaseType.etrade);
        OrderInfo orderInfo_etrade = orderInfoMapper.selectByPrimaryKey("20170710121208001");
        System.out.println("***************************************");
        System.out.println(orderInfo_etrade.toString());
        System.out.println("***************************************");
        DatabaseContextHolder.setDatabaseType(DatabaseType.sale);
        OrderInfo orderInfo_sale = orderInfoMapper.selectByPrimaryKey("20170710121208001");
        System.out.println("***************************************");
        System.out.println(orderInfo_sale.toString());
        System.out.println("***************************************");
    }

    @Test
    public void testTransaction(){
        orderService.addOrder();
    }
}
