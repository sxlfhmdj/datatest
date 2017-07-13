package org.xiaod.datatest.distribute;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.xiaod.datatest.distribute.config.EtradeDataProperties;
import org.xiaod.datatest.distribute.config.SaleDataProperties;

/**
 * Discription: [SpringBoot Main]
 * Created on: 15:52 2017/7/10
 */

@Configuration
@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JtaAutoConfiguration.class})
@ComponentScan("org.xiaod.datatest.distribute")
@EnableConfigurationProperties({EtradeDataProperties.class,SaleDataProperties.class})
@MapperScan(basePackages = {"org.xiaod.datatest.distribute.dao.etrade.iface",
        "org.xiaod.datatest.distribute.dao.sale.iface"})
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
