package com.github.skysilently.dingtalk.collector.plugingetter.schedule;

import com.github.skysilently.dingtalk.collector.plugingetter.nginx.NginxItemUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@Component
@EnableScheduling
public class NginxSchedule {

    static {
        log.info("NginxSchedule loaded");
    }

    @Autowired
    private NginxItemUpdater nginxItemUpdater;

//    @Scheduled(cron = "1-30 * * * * ?") //测试:每分钟1-30秒执行
//@Scheduled(cron = "0 0 1 * * ?") //每日1点执行
    public void checkFastjsonUpdate(){
        nginxItemUpdater.checkUpdate();
        log.info("NginxSchedule run");
    }

}
