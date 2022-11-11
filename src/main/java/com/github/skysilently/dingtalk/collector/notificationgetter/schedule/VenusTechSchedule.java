package com.github.skysilently.dingtalk.collector.notificationgetter.schedule;

import com.github.skysilently.dingtalk.collector.notificationgetter.venustech.VenusTechUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class VenusTechSchedule {

    static {
        log.info("VenusTechSchedule loaded");
    }

    @Autowired
    private VenusTechUpdater venusTechUpdater;

//    @Scheduled(cron = "0 0 6,8,9,10,11,13,14,15,16,17,18,20,22,23 * * ?")
    public void checkQiHooUpdate(){
        venusTechUpdater.checkUpdate();
        log.info("VenusTechSchedule checkUpdate");
    }

}
