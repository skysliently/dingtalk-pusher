package com.github.skysilently.dingtalk.collector.plugingetter.schedule;

import com.github.skysilently.dingtalk.collector.plugingetter.xstream.XStreamItemUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class XStreamSchedule {

    static {
        log.info("XStreamSchedule loaded");
    }

    @Autowired
    private XStreamItemUpdater xStreamItemUpdater;

//    @Scheduled(cron = "0 0 1 * * ?") //每日1点执行
    public void checkFastjsonUpdate(){
        xStreamItemUpdater.checkUpdate();
        xStreamItemUpdater.checkDependencyVersion();
        log.info("XStreamSchedule run");
    }

}
