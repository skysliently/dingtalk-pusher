package com.github.skysilently.dingtalk.collector.msgpusher.schedule;

import com.github.skysilently.dingtalk.collector.msgpusher.DingTalkPusher;
import com.github.skysilently.dingtalk.collector.msgpusher.hupu.HuPuPusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author keyun
 * @create 2022-11-11 16:09
 **/
@Slf4j
@Component
@EnableScheduling
public class HuPuSchedule {

    static {
        log.info("HuPuSchedule loaded");
    }

    @Autowired
    HuPuPusher pusher;

    @Scheduled(cron = "0 0 10,11,14,16,15,20 * * 1/5")
    public void push(){
        try {
            Map<String, String> resultMap = pusher.getHuPuFromJsoup();
            DingTalkPusher.dingTalkPush("虎扑热榜",resultMap);
        } catch (IOException e) {
            log.error("获取虎扑热榜失败",e);
            return;
        }
        log.info("HuPuSchedule push");
    }

}
