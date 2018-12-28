package com.frank.zheng.tinyurl.dao;

import com.frank.zheng.tinyurl.TinyUrlApplication;
import com.frank.zheng.tinyurl.entity.TinyUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TinyUrlApplication.class)
public class TinyURLDaoTest {

    @Autowired
    private TinyURLDao tinyURLDao;

    @Test
    public void testFindById() {
        TinyUrl url = TinyUrl.createByOriginalUrl("https://www.google.com");
        int rows = tinyURLDao.insert(url);
        System.out.println("insert " + rows + " rows");

        System.out.println(url.getId());
        TinyUrl tinyUrl = tinyURLDao.findById(url.getId());
        System.out.println(tinyUrl.getOriginal_url());
    }

    @Test
    public void testUpdateTinyUrlById() {
        TinyUrl tinyUrl = new TinyUrl();
        tinyUrl.setId(2);
        tinyUrl.setTiny_url("abcdefg");
        tinyURLDao.updateTinyUrl(tinyUrl);

    }


}
