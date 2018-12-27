package com.frank.zheng.tinyurl.cache;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@RunWith(BlockJUnit4ClassRunner.class)
public class RedisServiceTest {

    private RedisService redisService;

    @Before
    public void setUp() {
        redisService = new RedisService();
    }

    @After
    public void tearDown() {
        redisService = null;
    }

    @Test
    public void testRedis() {
        try (Jedis jedis = redisService.getResource()) {
            jedis.hset("tiny_urls", "abcd", "https://www.google.com");
            String url = jedis.hget("tiny_urls", "abcd");
            System.out.println(url);
        }
    }


}
