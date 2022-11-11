package com.github.skysilently.dingtalk.service.impl;

import com.github.skysilently.dingtalk.mapper.SecItemListMapper;
import com.github.skysilently.dingtalk.module.dao.SecItemListDO;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.service.api.SecItemListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SecItemListServicelmpl implements SecItemListService {

    @Autowired
    private SecItemListMapper secItemListMapper;

    public SecItemListModel getLastSecItemList(String plugin) {
        try {
            SecItemListDO secItemListDO = secItemListMapper.getLastSecItemList(plugin);
            if (secItemListDO == null) {
                return null;
            }
            SecItemListModel secItemListModel = getSecItemListModel(secItemListDO);
//            SecItemListModel secItemListModel;
            return secItemListModel;
        } catch (Exception e){
            log.error("系统异常！", e);
//            return new SecItemListModel();
            return null;
        }
    }

    private SecItemListModel getSecItemListModel(SecItemListDO secItemListDO) {
        SecItemListModel secItemListModel = new SecItemListModel();
        secItemListModel.setId(secItemListDO.getId());
        secItemListModel.setPlugin(secItemListDO.getPlugin());
        secItemListModel.setItem(secItemListDO.getItem());
        secItemListModel.setCreateTime(secItemListDO.getCreateTime());
        secItemListModel.setSecInformLink(secItemListDO.getSecInformLink());
        secItemListModel.setRisk(secItemListDO.getRisk());
        secItemListModel.setDescription(secItemListDO.getDescription());
        secItemListModel.setCveId(secItemListDO.getCveId());
        secItemListModel.setPublishTime(secItemListDO.getPublishTime());
        secItemListModel.setPublishTimeOrigin(secItemListDO.getPublishTimeOrigin());
        secItemListModel.setVersionRange(secItemListDO.getVersionRange());
        return secItemListModel;
    }

    public List<SecItemListModel> getLast3SecItemList(String plugin) {
        try {
            List<SecItemListDO> secItemListDOList = secItemListMapper.getLast3SecItemList(plugin);
            return getSecItemListModelList(secItemListDOList);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    public List<SecItemListModel> getAllSecItemList(String plugin) {
        try {
            List<SecItemListDO> secItemListDOList = secItemListMapper.getAllSecItemList(plugin);
            return getSecItemListModelList(secItemListDOList);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    private List<SecItemListModel> getSecItemListModelList(List<SecItemListDO> secItemListDOList) {
        List<SecItemListModel> secItemListModelList = new ArrayList<>();
        for (SecItemListDO secItemListDO :
                secItemListDOList) {
           SecItemListModel secItemListModel = getSecItemListModel(secItemListDO);
            secItemListModelList.add(secItemListModel);
        }
        if (secItemListModelList.isEmpty()) {
            return null;
        }
        return secItemListModelList;
    }

    public int addSecItem(SecItemListModel secItemListModel) {
        try {
            SecItemListDO secItemListDO = new SecItemListDO();
            secItemListDO.setPlugin(secItemListModel.getPlugin());
            secItemListDO.setItem(secItemListModel.getItem());
            secItemListDO.setCreateTime(secItemListModel.getCreateTime());
            secItemListDO.setSecInformLink(secItemListModel.getSecInformLink());
            secItemListDO.setRisk(secItemListModel.getRisk());
            secItemListDO.setDescription(secItemListModel.getDescription());
            secItemListDO.setCveId(secItemListModel.getCveId());
            secItemListDO.setPublishTime(secItemListModel.getPublishTime());
            secItemListDO.setPublishTimeOrigin(secItemListModel.getPublishTimeOrigin());
            secItemListDO.setVersionRange(secItemListModel.getVersionRange());
            secItemListMapper.addSecItem(secItemListDO);
            return 0;
        } catch (Exception e) {
            log.error("系统异常！", e);
            return -1;
        }
    }

    public int addSecItems(List<SecItemListModel> secItemListModelList) {
        for (SecItemListModel secItemListModel:
                secItemListModelList) {
            if (addSecItem(secItemListModel) == -1) {
                return -1;
            }
        }
        return 0;
    }

}
