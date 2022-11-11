package com.github.skysilently.dingtalk.service.api;

import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;

import java.util.List;

public interface NotificationService {

    List<NotificationModel> getAllNotifications();

    int addNotification(NotificationModel notificationModel);

    List<NotificationModel> getNotificationsBySourceAndTypeAndLimitNum(String source, String type, int limitNum);

    List<NotificationModel> getWarningNotificationsWithoutSelfLimit2Days(String source);

    boolean addNotifications(List<NotificationModel> notificationModelArrayList);

}
