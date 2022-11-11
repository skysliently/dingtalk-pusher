package com.github.skysilently.dingtalk.collector.notificationgetter.schedule;

import com.github.skysilently.dingtalk.collector.notificationgetter.cnnvd.CNNVDUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author keyun
 * @create 2022-06-14 14:07
 **/
@Slf4j
//@Component
@EnableScheduling
public class CNNVDSchedule {

    static {
        log.info("CNNVDSchedule loaded");
    }

    @Autowired
    private CNNVDUpdater cnnvdUpdater;

//    @Scheduled(cron = "0 0 14 * * ?")
    public void checkQiHooUpdate(){
        cnnvdUpdater.checkUpdate();
        log.info("CNNVDSchedule checkUpdate");
    }

}
