package com.github.skysilently.dingtalk.collector.notificationgetter.schedule.community;

import com.github.skysilently.dingtalk.collector.notificationgetter.xianzhi.XianZhiUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author keyun
 * @create 2022-11-03 19:50
 **/
@Slf4j
@Component
@EnableScheduling
public class XianZhiSchedule {

    static {
        log.info("XianZhiSchedule loaded");
    }

    @Autowired
    XianZhiUpdater updater;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkUpdate(){
        updater.checkUpdate();
        log.info("XianZhiSchedule checkUpdate");
    }

}
