package org.xiaod.datatest.cache.redis.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.xiaod.datatest.cache.redis.RedisRepository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Description: 【缓存服务实现】 <br/>
 * Created on 17:46 2017/5/5 <br/>
 */

@Repository
public class RedisServiceImpl implements RedisRepository {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public String get(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        return value;
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void del(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public void setAndExpire(String key, Object value, int seconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void flushDB() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    public void tailPush(String key, Object object) {
        redisTemplate.opsForList().rightPush(key, object);
    }

    public void headPush(String key, Object object) {
        redisTemplate.opsForList().leftPush(key, object);
    }

}
