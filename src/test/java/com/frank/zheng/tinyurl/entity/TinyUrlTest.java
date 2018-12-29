package com.frank.zheng.tinyurl.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TinyUrlTest {

    @Test
    public void testToString() {
        TinyUrl url = new TinyUrl();
        url.setId(1234);
        url.setOriginal_url("https://www.google.com");
        url.setTiny_url("abcdd");

        System.out.println(url);
    }

}
