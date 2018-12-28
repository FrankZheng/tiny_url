package com.frank.zheng.tinyurl.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisService implements InitializingBean {

    @Value("${redis.server.url}") //read from application.properties
    private String redisServerUrl;

    private JedisPool pool;


    //visible for testing
    RedisService() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool(new JedisPoolConfig(), redisServerUrl);
    }

    public Jedis getResource() {
        return pool.getResource();
    }
}
