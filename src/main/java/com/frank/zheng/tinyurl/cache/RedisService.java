package com.frank.zheng.tinyurl.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisService implements InitializingBean {
    private JedisPool pool;


    //visible for testing
    RedisService() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    public Jedis getResource() {
        return pool.getResource();
    }
}
