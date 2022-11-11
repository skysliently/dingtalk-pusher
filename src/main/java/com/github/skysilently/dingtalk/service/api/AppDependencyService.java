package com.github.skysilently.dingtalk.service.api;

import com.github.skysilently.dingtalk.module.dao.AppDependencyDO;
import com.github.skysilently.dingtalk.module.dto.module.AppDependencyModel;

import java.util.List;

public interface AppDependencyService {

    List<AppDependencyModel> GetAppDependencyByArtifactId(String dependencyJarArtifactId);

    List<AppDependencyDO> getAppDependencyByArtifactId(String dependencyJarArtifactId);
}
