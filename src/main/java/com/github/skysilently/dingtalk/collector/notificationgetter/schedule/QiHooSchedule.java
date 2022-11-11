package com.github.skysilently.dingtalk.collector.notificationgetter.schedule;

import com.github.skysilently.dingtalk.collector.notificationgetter.qihoo.QiHooDailyUpdater;
import com.github.skysilently.dingtalk.collector.notificationgetter.qihoo.QiHooUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class QiHooSchedule {

    static {
        log.info("QiHooSchedule loaded");
    }

    @Autowired
    private QiHooUpdater qiHooUpdater;
    @Autowired
    private QiHooDailyUpdater qiHooDailyUpdater;

//    @Scheduled(cron = "0 0 6,8,9,10,11,13,14,15,16,17,18,20,22,23 * * ?")
    public void checkQiHooUpdate(){
        qiHooUpdater.checkUpdate();
        log.info("QiHooSchedule checkUpdate");
    }

//    @Scheduled(cron = "0 0 18 * * ?")
    public void checkQiHooDailyUpdate(){
        qiHooDailyUpdater.pushDaily();
        log.info("QiHooSchedule pushDaily");
    }

}
