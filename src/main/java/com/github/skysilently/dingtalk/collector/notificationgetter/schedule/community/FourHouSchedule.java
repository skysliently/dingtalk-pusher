package com.github.skysilently.dingtalk.collector.notificationgetter.schedule.community;

import com.github.skysilently.dingtalk.collector.notificationgetter.fourhou.FourHouUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author keyun
 * @create 2022-11-03 17:32
 **/
@Slf4j
//@Component
@EnableScheduling
public class FourHouSchedule {

    static {
        log.info("FourHouSchedule loaded");
    }

    @Autowired
    FourHouUpdater updater;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkUpdate(){
        updater.checkUpdate();
        log.info("FourHouSchedule checkUpdate");
    }

}
