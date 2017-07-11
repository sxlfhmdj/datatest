package org.xiaod.datatest.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Description: 【EtradeDataSource Config】 <br/>
 * Created on 16:52 2017/7/10 <br/>
 */

@Configuration
@MapperScan(basePackages = "org.xiaod.datatest.dao.etrade.iface", sqlSessionFactoryRef = "etradeSqlSessionFactory")
public class EtradeDataSourceConfig {
    @Bean(name = "etradeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.etrade")
    @Primary
    public DataSource etradeDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "etradeSqlSessionFactory")
    @Primary
    public SqlSessionFactory etradeSqlSessionFactory(@Qualifier("etradeDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] mapperLocations= new PathMatchingResourcePatternResolver().getResources("classpath:org/xiaod/datatest/dao/etrade/xml/*.xml");
        bean.setMapperLocations(mapperLocations);
        return bean.getObject();
    }
}
