package com.github.skysilently.dingtalk.collector.module;

import lombok.Data;

@Data
public class TencentResp {
    private Props props;
    private String page;
    private Object query;
    private String buildId;
    private Boolean isFallback;
    private Boolean gssp;
}
