# Data And Transaction

## Spring Boot

### 1、Spring Boot整合多数据源（MySql）

spring boot提供的默认配置是单数据源的配置，如果需要整合多数据源，则需要自定义配置,然后通过配置类将javabean与指定数据库关联

```
#多数据源配置
#Etrade DataSource
spring.datasource.etrade.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.etrade.url= jdbc:mysql://10.6.60.145:3306/etrade?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
spring.datasource.etrade.username=etrade
spring.datasource.etrade.password=11112222
#Sale DataSource
spring.datasource.sale.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.sale.url= jdbc:mysql://10.6.60.145:3306/sale?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
spring.datasource.sale.username=sale
spring.datasource.sale.password=11112222

#多ORM配置 不配置mapperLocation，在配置类中配置
mybatis.cacheEnabled: true
mybatis.lazyLoadingEnabled: true
mybatis.useGeneratedKeys: false
mybatis.autoMappingBehavior: PARTIAL
mybatis.lazyLoadTriggerMethods: equals,clone,hashCode,toString
mybatis.jdbcTypeForNull: NULL
mybatis.defaultStatementTimeout: 25000
```

```
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
```


```
create database etrade;
create database sale;
SELECT DISTINCT CONCAT('User: ''',user,'''@''',host,''';') AS query FROM mysql.user;
GRANT ALL PRIVILEGES ON etrade.* TO 'etrade'@'%' IDENTIFIED BY '11112222' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON sale.* TO 'sale'@'%' IDENTIFIED BY '11112222' WITH GRANT OPTION;
```


### 2、Spring Boot整合多数据源自动切换（MySql）
1. 通过本地线程，保证线程安全的前提下，保存线程的查找键（与数据源对应的key）。
1. 通过AbstractRoutingDataSource（路由数据源），并实现方法determineCurrentLookupKey获取当前的数据源查找键。
1. 对每个数据源声明定义DataSource（异名）
1. 声明路由数据源DynamicDataSource（默认Primary），指定多数据源键值对，和默认数据源。
1. 声明定义SqlSessionFactory和TransactionManagerment

```
/**
 * Description: 【数据源类型Key】 <br/>
 * Created on 10:14 2017/7/12 <br/>
 *
 * 这里的数据源和数据库是一对一的
 * DatabaseType中的变量名称就是数据库的名称
 */
public enum DatabaseType {
    etrade,
    sale
}
```

```
/**
 * Description: 【DatabaseType容器】 <br/>
 * Created on 10:17 2017/7/12 <br/>
 *
 * 保存一个线程安全的DatabaseType容器
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<DatabaseType>();

    public static void setDatabaseType(DatabaseType type){
        contextHolder.set(type);
    }

    public static DatabaseType getDatabaseType(){
        return contextHolder.get();
    }
}

```


```
/**
 * Description: 【动态数据源】 <br/>
 * Created on 10:24 2017/7/12 <br/>
 *
 * 使用DatabaseContextHolder获取当前线程的DatabaseType
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getDatabaseType();
    }
}
```



```
package org.xiaod.datatest.dynamic.config;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.xiaod.datatest.dynamic.common.datasource.DatabaseType;
import org.xiaod.datatest.dynamic.common.datasource.DynamicDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 【DynamicDatSource Config】 <br/>
 * Created on 10:27 2017/7/12 <br/>
 *
 * 配置动态切换
 *
 */
@Configuration
@MapperScan(basePackages = "org.xiaod.datatest.dynamic.dao.etrade.iface", sqlSessionFactoryRef = "dynamicSqlSessionFactory")
public class DynamicDataSourceConfig {

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

    /**
     * 配置事务管理器
     */
    @Bean(name = "dynamicTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dynamicDataSource") DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
```


### 3、数据库的切分，分布式事务
①表的垂直切分
②表的水平切分
分布式事务的实现，这里是通过Atomikos事务资源管理器来实现的

```
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

```

### 4、Redis事务与管道

### 5、参考资料
- http://www.cnblogs.com/ityouknow/p/4977136.html
spring 多数据源
- http://www.cnblogs.com/winner-0715/p/6687247.html
spring boot多数据源
- http://www.cnblogs.com/java-zhao/p/5413845.html
spring boot数据库动态切换
- http://www.cnblogs.com/java-zhao/p/5415896.html
spring boot数据库动态切换AOP实现
- http://blog.csdn.net/bluishglc/article/details/6161475
sharding
- http://blog.csdn.net/uestc_lxp/article/details/50456721
事务@Transaction
- http://blog.csdn.net/moonpure/article/details/53148593
分布式事务管理

### 6、笔记
使用多数据库最好把DataSourceAutoConfiguration屏蔽掉

使用@ConfigerationProperties注解读取配置文件，要在入口标明@EnableConfigurationProperties({EtradeDataProperties.class,SaleDataProperties.class})

Transaction事务注解最好使用使用在类和实现方法上，不要使用在接口和接口方法上。只有对接口代理的时候，使用在接口和接口方法上的Transaction才生效
Transaction事务注解只是针对类中的public方法生效，对于private和protected即使使用了也不会生效
Transaction事务注解类中方法之间的调用事务不会起作用，只是对外部的调用才会起作用
使用Transaction时，要启用Transaction即 @EnableTransactionManagement注解在Application上并且操作的数据库要支持事务回滚，即数据库引擎是innoDB