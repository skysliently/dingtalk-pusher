package com.github.skysilently.dingtalk.service.impl;

import com.github.skysilently.dingtalk.mapper.AppDependencyMapper;
import com.github.skysilently.dingtalk.module.dao.AppDependencyDO;
import com.github.skysilently.dingtalk.module.dto.module.AppDependencyModel;
import com.github.skysilently.dingtalk.service.api.AppDependencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppDependencyServiceImpl implements AppDependencyService {

    @Autowired
    private AppDependencyMapper appDependencyMapper;

    public List<AppDependencyDO> getAppDependencyByArtifactId(String dependencyJarArtifactId) {
        try {
            return appDependencyMapper.getAppDependencyByArtifactId(dependencyJarArtifactId);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    public List<AppDependencyModel> GetAppDependencyByArtifactId(String dependencyJarArtifactId) {
        try {
            List<AppDependencyDO> appDependencyDOList = appDependencyMapper.getAppDependencyByArtifactId(dependencyJarArtifactId);
            List<AppDependencyModel> appDependencyModelList = new ArrayList<>();
            for (AppDependencyDO appDependencyDO :
                    appDependencyDOList) {
                AppDependencyModel appDependencyModel = new AppDependencyModel();
                appDependencyModel.setId(appDependencyDO.getId());
                appDependencyModel.setEnv(appDependencyDO.getEnv());
                appDependencyModel.setAgentVersion(appDependencyDO.getAgentVersion());
                appDependencyModel.setDependencyJarArtifactId(appDependencyDO.getDependencyJarArtifactId());
                appDependencyModel.setDependencyJarGroupId(appDependencyDO.getDependencyJarGroupId());
                appDependencyModel.setDependencyJarVersion(appDependencyDO.getDependencyJarVersion());
                appDependencyModel.setAppName(appDependencyDO.getAppName());
                appDependencyModelList.add(appDependencyModel);
            }
            return appDependencyModelList;
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

}
