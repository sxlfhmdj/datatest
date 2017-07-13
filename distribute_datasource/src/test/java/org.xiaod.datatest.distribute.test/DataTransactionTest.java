package org.xiaod.datatest.distribute.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xiaod.datatest.distribute.service.OrderService;


/**
 * Description: 【测试】 <br/>
 * Created on 9:27 2017/7/11 <br/>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DataTransactionTest {

    @Autowired
    private OrderService orderService;

    @Test

    public void testTransaction(){
        orderService.addOrder();
    }
}
