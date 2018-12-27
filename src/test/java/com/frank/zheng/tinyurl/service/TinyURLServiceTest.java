package com.frank.zheng.tinyurl.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TinyURLServiceTest {
    TinyURLService service;


    @Before
    public void setUp() {
        service = new TinyURLService();
    }

    @After
    public void tearDown() {
        service = null;
    }

    @Test
    public void testIdToTinyURL() {
        int id = 1231234324;
        System.out.println(service.idToTinyURL(id));
    }

    @Test
    public void testTinyUrlToID() {
        String url = "bvuisi";
        System.out.println(service.tinyUrlToID(url));
    }


}
