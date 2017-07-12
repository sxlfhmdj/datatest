package org.xiaod.datatest.dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Discription: [SpringBoot Main]
 * Created on: 15:52 2017/7/10
 */

@Configuration
@EnableTransactionManagement
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("org.xiaod.datatest.dynamic")
//@PropertySource("classpath:spring_config/spring-config.xml")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
