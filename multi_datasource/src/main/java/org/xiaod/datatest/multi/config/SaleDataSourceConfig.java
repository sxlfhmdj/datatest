package org.xiaod.datatest.multi.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Description: 【SaleDataSource Config】 <br/>
 * Created on 16:52 2017/7/10 <br/>
 */

@Configuration
@MapperScan(basePackages = "org.xiaod.datatest.multi.dao.sale.iface", sqlSessionFactoryRef = "saleSqlSessionFactory")
public class SaleDataSourceConfig {
    @Bean(name = "saleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sale")
    public DataSource saleDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "saleSqlSessionFactory")
    public SqlSessionFactory saleSqlSessionFactory(@Qualifier("saleDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:org/xiaod/datatest/multi/dao/sale/xml/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "saleTransactionManager")
    @Primary
    public DataSourceTransactionManager saleTransactionManager(@Qualifier("saleDataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
