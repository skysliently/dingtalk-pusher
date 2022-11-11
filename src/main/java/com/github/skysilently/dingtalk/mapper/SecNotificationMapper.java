package com.github.skysilently.dingtalk.mapper;

import com.github.skysilently.dingtalk.module.dao.SecNotificationDAO;

import java.util.List;

public interface SecNotificationMapper {

    List<SecNotificationDAO> listLastTenNote();

}
