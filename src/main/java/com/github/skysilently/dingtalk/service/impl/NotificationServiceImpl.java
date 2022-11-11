package com.github.skysilently.dingtalk.service.impl;

import com.github.skysilently.dingtalk.mapper.NotificationMapper;
import com.github.skysilently.dingtalk.module.dao.NotificationDO;
import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.github.skysilently.dingtalk.service.api.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public List<NotificationModel> getAllNotifications() {
        try {
            List<NotificationDO> notificationDOList = notificationMapper.getAllNotifications();
            return getNotificationModelList(notificationDOList);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    private List<NotificationModel> getNotificationModelList(List<NotificationDO> notificationDOList) {
        List<NotificationModel> notificationModelList = new ArrayList<>();
        for (NotificationDO notificationDO :
                notificationDOList) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setId(notificationDO.getId());
            notificationModel.setSource(notificationDO.getSource());
            notificationModel.setInfoTitle(notificationDO.getInfoTitle());
            notificationModel.setType(notificationDO.getType());
            notificationModel.setLink(notificationDO.getLink());
            notificationModel.setDescription(notificationDO.getDescription());
            notificationModel.setPublishDate(notificationDO.getPublishDate());
            notificationModel.setCreateTime(notificationDO.getCreateTime());
            notificationModel.setUpdateTime(notificationDO.getUpdateTime());
            notificationModel.setDetailImg(notificationDO.getDetailImg());
            notificationModelList.add(notificationModel);
        }
        return notificationModelList;
    }

    public int addNotification(NotificationModel notificationModel) {
        NotificationDO notificationDO = new NotificationDO();
        notificationDO.setSource(notificationModel.getSource());
        notificationDO.setInfoTitle(notificationModel.getInfoTitle());
        notificationDO.setType(notificationModel.getType());
        notificationDO.setLink(notificationModel.getLink());
        notificationDO.setDescription(notificationModel.getDescription());
        notificationDO.setPublishDate(notificationModel.getPublishDate());
        notificationDO.setCreateTime(notificationModel.getCreateTime());
        notificationDO.setUpdateTime(notificationModel.getUpdateTime());
        notificationDO.setDetailImg(notificationModel.getDetailImg());
        try {
            notificationMapper.addNotification(notificationDO);
            return 1;
        } catch (Exception e) {
            log.error("系统异常！", e);
            return -1;
        }
    }

    public List<NotificationModel> getNotificationsBySourceAndTypeAndLimitNum(String source, String type, int limitNum) {
        try {
            List<NotificationDO> notificationDOList = notificationMapper.getNotificationsBySourceAndTypeAndLimitNum(source,type,limitNum);
            return getNotificationModelList(notificationDOList);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    public List<NotificationModel> getWarningNotificationsWithoutSelfLimit2Days(String source) {
        try {
            List<NotificationDO> notificationDOList = notificationMapper.getWarningNotificationsWithoutSelfLimit2Days(source);
            return getNotificationModelList(notificationDOList);
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }

    public boolean addNotifications(List<NotificationModel> notificationModelList) {
        for (NotificationModel notificationModel :
                notificationModelList ) {
            if (addNotification(notificationModel) == -1)
                return false;
        }
        return true;
    }

}
