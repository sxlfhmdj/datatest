package org.xiaod.datatest.distribute.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.xiaod.datatest.distribute.common.datasource.DatabaseType;
import org.xiaod.datatest.distribute.common.datasource.DynamicDataSource;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 【DistributeDatSource Config】 <br/>
 * Created on 10:27 2017/7/12 <br/>
 *
 * 分布式事务管理器
 *
 */
@Configuration
@MapperScan(basePackages = {"org.xiaod.datatest.dynamic.dao.etrade.iface"}, sqlSessionFactoryRef = "dynamicSqlSessionFactory")
public class DistributeDataSourceConfig {

    @Bean(name = "etradeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.etrade")
    public DataSource etradeDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "saleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sale")
    public DataSource saleDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(@Qualifier("etradeDataSource") DataSource etradeDataSource,
                                        @Qualifier("saleDataSource") DataSource saleDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put(DatabaseType.etrade, etradeDataSource);
        targetDataSources.put(DatabaseType.sale, saleDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(etradeDataSource);// 默认的datasource设置为etradeDataSource
        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name = "dynamicSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        Resource[] mapperLocations= new PathMatchingResourcePatternResolver().getResources("classpath:org/xiaod/datatest/dynamic/dao/etrade/xml/*.xml");
        bean.setMapperLocations(mapperLocations);
        return bean.getObject();
    }

    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn({ "userTransaction", "atomikosTransactionManager" })
    public PlatformTransactionManager transactionManager() throws Throwable {
        UserTransaction userTransaction = userTransaction();
        JtaTransactionManager manager = new JtaTransactionManager(userTransaction, atomikosTransactionManager());
        return manager;
    }



}
