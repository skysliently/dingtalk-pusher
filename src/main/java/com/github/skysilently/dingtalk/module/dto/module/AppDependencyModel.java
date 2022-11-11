package com.github.skysilently.dingtalk.module.dto.module;

import lombok.Data;

import java.io.Serializable;

/**
 * chongtong_app_dependency_info
 * 应用依赖测试表
 */
@Data
public class AppDependencyModel implements Serializable {

    private Long id;

    private String env;

    private String agentVersion;

    private String dependencyJarGroupId;

    private String dependencyJarArtifactId;

    private String dependencyJarVersion;

    private String AppName;

}
