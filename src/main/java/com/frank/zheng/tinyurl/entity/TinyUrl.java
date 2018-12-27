package com.frank.zheng.tinyurl.entity;

import org.springframework.lang.NonNull;

public class TinyUrl {
    private int id;
    private String original_url;
    private String tiny_url;

    public static TinyUrl createByOriginalUrl(@NonNull String originalUrl) {
        TinyUrl url = new TinyUrl();
        url.setOriginal_url(originalUrl);
        return url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getTiny_url() {
        return tiny_url;
    }

    public void setTiny_url(String tiny_url) {
        this.tiny_url = tiny_url;
    }
}
