package com.github.skysilently.dingtalk.collector.module;

import lombok.Data;

@Data
public class Item {
    private String author;
    private Integer catid;
    private String description;
    private Integer id;
    private String inputip;
    private Integer inputtime;
    private Integer updatetime;
    private String seoDescription;
    private String title;
    private String url;
    private String thumb;
}
