package com.frank.zheng.tinyurl.web.controller;

import com.frank.zheng.tinyurl.dao.TinyURLDao;
import com.frank.zheng.tinyurl.entity.TinyUrl;
import com.frank.zheng.tinyurl.service.TinyURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tiny_url")
public class TinyURLController {

    @Autowired
    private TinyURLDao tinyURLDao;

    @Autowired
    private TinyURLService tinyURLService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TinyUrl createTinyUrl(@RequestParam("originalUrl") String originalUrl ) {
        System.out.println(originalUrl);
        //TODO: validate parameters
        //Figure out a more elegant way to return error response
        if (originalUrl == null || originalUrl.length() == 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        return tinyURLService.createTinyUrl(originalUrl);
    }

    @RequestMapping(value = "/{tinyUrl}", method = RequestMethod.GET)
    public TinyUrl getOriginalUrl(@PathVariable String tinyUrl) {
        if (tinyUrl == null || tinyUrl.length() == 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        int id = tinyURLService.tinyUrlToID(tinyUrl);
        return tinyURLDao.findById(id);
    }





}
