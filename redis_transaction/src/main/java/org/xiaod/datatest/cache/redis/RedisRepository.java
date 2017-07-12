package org.xiaod.datatest.cache.redis;

/**
 * Description: 【缓存服务接口】 <br/>
 * Created on 17:59 2017/5/5 <br/>
 */
public interface RedisRepository {

    public boolean exists(String key);

    public String get(String key);

    public void set(String key, Object value);

    public void del(String key);

    public void setAndExpire(String key, Object value, int seconds);

    public void flushDB();

    public void tailPush(String key, Object object);

    public void headPush(String key, Object object);

}
