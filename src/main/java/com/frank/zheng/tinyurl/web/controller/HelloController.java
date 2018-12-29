package com.frank.zheng.tinyurl.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("hello")
    public String index() {
        return "hello world";
    }

    @RequestMapping(value = "post", method = RequestMethod.POST, produces = "application/json")
    public String post(@RequestBody Map<String, Object> params) {
        logger.info("post request: " + params);
        return "OK";
    }
}
