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


### 3、Spring Boot处理多数据库事务



### 4、Spring Boot处理数据库和Redis事务

### 5、参考资料
- http://www.cnblogs.com/ityouknow/p/4977136.html
- http://www.cnblogs.com/winner-0715/p/6687247.html
- http://www.cnblogs.com/java-zhao/p/5413845.html
- http://www.cnblogs.com/java-zhao/p/5415896.html
- http://blog.csdn.net/bluishglc/article/details/6161475
事务
- http://blog.csdn.net/uestc_lxp/article/details/50456721

### 6、笔记
使用多数据库最好把DataSourceAutoConfiguration屏蔽掉

Transaction事务注解最好使用使用在类和实现方法上，不要使用在接口和接口方法上。只有对接口代理的时候，使用在接口和接口方法上的Transaction才生效
Transaction事务注解只是针对类中的public方法生效，对于private和protected即使使用了也不会生效
Transaction事务注解类中方法之间的调用事务不会起作用，只是对外部的调用才会起作用
使用Transaction时，要启用Transaction即 @EnableTransactionManagement注解在Application上并且操作的数据库要支持事务回滚，即数据库引擎是innoDB