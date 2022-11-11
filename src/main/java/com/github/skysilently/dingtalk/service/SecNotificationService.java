package com.github.skysilently.dingtalk.service;

import com.github.skysilently.dingtalk.module.dao.SecNotificationDAO;
import com.github.skysilently.dingtalk.mapper.SecNotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SecNotificationService {

    @Autowired
    private SecNotificationMapper secNotificationMapper;

    public List<SecNotificationDAO> listLastTenNote(){
        try {
            return secNotificationMapper.listLastTenNote();
        } catch (Exception e) {
            log.error("系统异常！", e);
            return null;
        }
    }


}
