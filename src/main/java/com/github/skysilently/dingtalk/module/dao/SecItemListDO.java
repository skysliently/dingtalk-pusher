package com.github.skysilently.dingtalk.module.dao;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class SecItemListDO implements Serializable {

    private Long id;

    private String plugin;

    private String item;

    private Date createTime;

    private String secInformLink;

    private String risk;

    private String description;

    private String cveId;

    private Date publishTime;

    private String publishTimeOrigin;

    private String versionRange;

}
