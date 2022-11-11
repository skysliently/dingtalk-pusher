package com.github.skysilently.dingtalk.module.dto.module;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class NotificationModel implements Serializable {

    private Long id;

    private String source;

    private String infoTitle;

    private String type;

    private String link;

    private String description;

    private Timestamp publishDate;

    private Date createTime;

    private Date updateTime;

    private String detailImg;
}
