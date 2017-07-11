# Data And Transaction



## Spring Boot



### 1、Spring Boot整合多数据源（MySql）

spring boot提供的默认配置是单数据源的配置，如果需要整合多数据源，则需要自定义配置,然后通过配置类将javabean与指定数据库关联



```

#多数据源配置

#Etrade DataSource

spring.datasource.etrade.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.etrade.url= jdbc:mysql://10.6.60.38:3306/etrade?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull

spring.datasource.etrade.type=com.alibaba.druid.pool.DruidDataSource

spring.datasource.etrade.data-username=root

spring.datasource.etrade.data-password=11112222

#Sale DataSource

spring.datasource.sale.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.sale.url= jdbc:mysql://10.6.60.38:3306/sale?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull

spring.datasource.sale.type=com.alibaba.druid.pool.DruidDataSource

spring.datasource.sale.data-username=root

spring.datasource.sale.data-password=11112222

```





```

@Configuration

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

bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:org/xiaod/datatest/dao/etrade/xml/*.xml"));

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

### 2、Spring Boot处理多数据库事务



### 3、Spring Boot处理数据库和Redis事务

### 4、参考资料
- http://www.cnblogs.com/ityouknow/p/4977136.html
- http://www.cnblogs.com/winner-0715/p/6687247.html
- http://www.cnblogs.com/java-zhao/p/5413845.html
- http://www.cnblogs.com/java-zhao/p/5415896.html

### 5、blog
- https://www.cnblogs.com/