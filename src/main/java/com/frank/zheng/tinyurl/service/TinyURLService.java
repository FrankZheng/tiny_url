package com.frank.zheng.tinyurl.service;

import com.frank.zheng.tinyurl.cache.RedisService;
import com.frank.zheng.tinyurl.dao.TinyURLDao;
import com.frank.zheng.tinyurl.entity.TinyUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


@Service
public class TinyURLService {

    private static final Logger logger = LoggerFactory.getLogger(TinyURLService.class);

    private static final String TINY_URLS_KEY_PREFIX = "tiny_urls_";

    @Autowired
    private TinyURLDao tinyURLDao;

    @Autowired
    private RedisService redisService;

    String idToTinyURL(int n) {
        // Map to store 62 possible characters
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder tinyUrl = new StringBuilder();

        // Convert given integer id to a base 62 number
        while (n > 0)
        {
            tinyUrl.insert(0, chars.charAt(n%62));
            n = n/62;
        }

        return tinyUrl.toString();
    }

    int tinyUrlToID(@NonNull String tinyUrl) {
        int id = 0; // initialize result

        // A simple base conversion logic
        for (int i=0; i < tinyUrl.length(); i++) {
            char ch = tinyUrl.charAt(i);
            if ('a' <= ch && ch <= 'z')
                id = id*62 + ch - 'a';
            if ('A' <= ch && ch <= 'Z')
                id = id*62 + ch - 'A' + 26;
            if ('0' <= ch && ch <= '9')
                id = id*62 + ch - '0' + 52;
        }
        return id;
    }

    public TinyUrl createTinyUrl(@NonNull String originalUrl) {
        TinyUrl tinyUrl = TinyUrl.createByOriginalUrl(originalUrl);
        tinyURLDao.insert(tinyUrl);
        String url = idToTinyURL(tinyUrl.getId());
        tinyUrl.setTiny_url(url);
        tinyURLDao.updateTinyUrl(tinyUrl);

        //save to redis
        saveToCache(tinyUrl);

        return tinyUrl;
    }

    public TinyUrl getTinyUrl(@NonNull String tinyUrl) {
        //first try to get redis from
        int id = tinyUrlToID(tinyUrl);
        TinyUrl tinyUrlObj = getFromCache(id);
        if (tinyUrlObj == null) {
            return tinyURLDao.findById(id);
        }
        return tinyUrlObj;
    }


    //save to redis
    private void saveToCache(@NonNull TinyUrl tinyUrl) {
        try (Jedis jedis = redisService.getResource()) {
            String key = TINY_URLS_KEY_PREFIX + tinyUrl.getId();
            jedis.hset(key, "original_url", tinyUrl.getOriginal_url());
            jedis.hset(key, "tiny_url", tinyUrl.getTiny_url());
        } catch (Exception e) {
            logger.error("Failed to save to redis", e);
        }
    }

    private TinyUrl getFromCache(int id) {
        try (Jedis jedis = redisService.getResource()) {
            String key = TINY_URLS_KEY_PREFIX + id;
            if (jedis.exists(key)) {
                String originalUrl = jedis.hget(key, "original_url");
                String tinyUrl = jedis.hget(key, "tiny_url");
                TinyUrl tinyUrlObj = new TinyUrl();
                tinyUrlObj.setOriginal_url(originalUrl);
                tinyUrlObj.setTiny_url(tinyUrl);
                return tinyUrlObj;
            }
        } catch (Exception e) {
            logger.error("Failed to load data from redis", e);
        }

        return null;
    }
}
