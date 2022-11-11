package com.github.skysilently.dingtalk.collector.plugingetter.schedule;

import com.github.skysilently.dingtalk.collector.plugingetter.tomcat.Tomcat7ItemUpdater;
import com.github.skysilently.dingtalk.collector.plugingetter.tomcat.Tomcat10ItemUpdater;
import com.github.skysilently.dingtalk.collector.plugingetter.tomcat.Tomcat8ItemUpdater;
import com.github.skysilently.dingtalk.collector.plugingetter.tomcat.Tomcat9ItemUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class TomcatSchedule {

    static {
        log.info("TomcatSchedule loaded");
    }

    @Autowired
    Tomcat7ItemUpdater tomcat7ItemUpdater;

    @Autowired
    Tomcat8ItemUpdater tomcat8ItemUpdater;

    @Autowired
    Tomcat9ItemUpdater tomcat9ItemUpdater;

    @Autowired
    Tomcat10ItemUpdater tomcat10ItemUpdater;

//    @Scheduled(cron = "0 0 1 * * ?") //每日1点执行
    public void checkFastjsonUpdate(){
        tomcat7ItemUpdater.checkUpdate();
        tomcat8ItemUpdater.checkUpdate();
        tomcat9ItemUpdater.checkUpdate();
        tomcat10ItemUpdater.checkUpdate();
        log.info("TomcatSchedule run");
    }

}
