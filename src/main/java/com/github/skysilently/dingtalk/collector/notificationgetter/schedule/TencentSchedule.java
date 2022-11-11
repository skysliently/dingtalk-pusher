package com.github.skysilently.dingtalk.collector.notificationgetter.schedule;

import com.github.skysilently.dingtalk.collector.notificationgetter.tencent.TencentUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class TencentSchedule {

    static {
        log.info("TencentSchedule loaded");
    }

    @Autowired
    private TencentUpdater tencentUpdater;

//    @Scheduled(cron = "0 0 6,8,9,10,11,13,14,15,16,17,18,20,22,23 * * ?")
    public void checkQiHooUpdate(){
        tencentUpdater.checkUpdate();
        log.info("TencentSchedule checkUpdate");
    }

}
