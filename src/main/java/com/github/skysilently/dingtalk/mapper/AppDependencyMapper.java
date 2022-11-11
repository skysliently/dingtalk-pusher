package com.github.skysilently.dingtalk.mapper;

import com.github.skysilently.dingtalk.module.dao.AppDependencyDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AppDependencyMapper {

    List<AppDependencyDO> getAppDependencyByArtifactId(@Param("dependencyJarArtifactId")String dependencyJarArtifactId);

}
