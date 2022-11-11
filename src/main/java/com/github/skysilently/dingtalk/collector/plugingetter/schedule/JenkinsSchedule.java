package com.github.skysilently.dingtalk.collector.plugingetter.schedule;

import com.github.skysilently.dingtalk.collector.plugingetter.jenkins.JenkinsItemUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class JenkinsSchedule {

    static {
        log.info("JenkinsSchedule loaded");
    }

    @Autowired
    private JenkinsItemUpdater jenkinsItemUpdater;

//    @Scheduled(cron = "0 0 1 * * ?") //每日1点执行
    public void checkFastjsonUpdate(){
        jenkinsItemUpdater.checkUpdate();
        log.info("JenkinsSchedule run");
    }

}
