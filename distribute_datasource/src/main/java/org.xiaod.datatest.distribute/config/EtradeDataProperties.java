package org.xiaod.datatest.distribute.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: 【Etrade data properties】 <br/>
 * Created on 9:32 2017/7/13 <br/>
 */
@ConfigurationProperties(prefix = "spring.datasource.etrade", locations={"classpath*:application.properties"})
public class EtradeDataProperties {
    private String username;
    private String password;
    private String driver;
    private String url;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
