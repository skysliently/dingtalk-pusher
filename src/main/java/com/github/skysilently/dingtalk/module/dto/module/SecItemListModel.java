package com.github.skysilently.dingtalk.module.dto.module;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class SecItemListModel implements Serializable {

    private Long id;

    private String plugin;

    private String item;

    private Date createTime;

    private String secInformLink;

    private String risk;

    private String description;

    private String cveId;

    //是否可以直接用字符串，减少一些麻烦
    private Date publishTime;

    private String publishTimeOrigin;

    private String versionRange;
}
