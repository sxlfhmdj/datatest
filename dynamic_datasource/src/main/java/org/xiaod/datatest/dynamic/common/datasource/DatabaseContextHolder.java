package org.xiaod.datatest.dynamic.common.datasource;

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
