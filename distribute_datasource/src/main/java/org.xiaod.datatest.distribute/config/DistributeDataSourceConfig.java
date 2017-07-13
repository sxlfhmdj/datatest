package org.xiaod.datatest.distribute.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
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
public class DistributeDataSourceConfig {

    @Autowired
    EtradeDataProperties etradeDataProperties;

    @Autowired
    SaleDataProperties saleDataProperties;

    @Bean(name = "etradeDataSource")
    @Primary
    public AtomikosDataSourceBean etradeDataSource(){
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(etradeDataProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(etradeDataProperties.getPassword());
        mysqlXaDataSource.setUser(etradeDataProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("etradeDataSource");

        return xaDataSource;
    }

    @Bean(name = "saleDataSource")
    public AtomikosDataSourceBean saleDataSource(){
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(saleDataProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(saleDataProperties.getPassword());
        mysqlXaDataSource.setUser(saleDataProperties.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("saleDataSource");

        return xaDataSource;
    }


    @Bean(name = "etradeSqlSessionFactory")
    @Primary
    public SqlSessionFactory etradeSqlSessionFactory(@Qualifier("etradeDataSource") AtomikosDataSourceBean dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] mapperLocations= new PathMatchingResourcePatternResolver().getResources("classpath*:org/xiaod/datatest/distribute/dao/etrade/xml/*.xml");
        bean.setMapperLocations(mapperLocations);
        return bean.getObject();
    }

    @Bean(name = "saleSqlSessionFactory")
    public SqlSessionFactory saleSqlSessionFactory(@Qualifier("saleDataSource") AtomikosDataSourceBean dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:org/xiaod/datatest/distribute/dao/sale/xml/*.xml"));
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
