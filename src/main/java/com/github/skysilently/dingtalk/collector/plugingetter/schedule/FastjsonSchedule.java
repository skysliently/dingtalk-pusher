package com.github.skysilently.dingtalk.collector.plugingetter.schedule;

import com.github.skysilently.dingtalk.collector.plugingetter.fastjson.FastjsonUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class FastjsonSchedule {

    static {
        log.info("FastjsonSchedule loaded");
    }

    @Autowired
    private FastjsonUpdater fastjsonUpdater;

//    @Scheduled(cron = "0 0 1 * * ?") //每日1点执行
    public void checkFastjsonUpdate(){
        fastjsonUpdater.checkUpdate();
        fastjsonUpdater.checkDependencyVersion();
        log.info("FastjsonSchedule run");
    }

}
