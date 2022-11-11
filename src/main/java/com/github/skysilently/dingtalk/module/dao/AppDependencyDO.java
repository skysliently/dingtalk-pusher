package com.github.skysilently.dingtalk.module.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class AppDependencyDO implements Serializable {

    private Long id;

    private String env;

    private String agentVersion;

    private String dependencyJarGroupId;

    private String dependencyJarArtifactId;

    private String dependencyJarVersion;

    private String appName;

}
