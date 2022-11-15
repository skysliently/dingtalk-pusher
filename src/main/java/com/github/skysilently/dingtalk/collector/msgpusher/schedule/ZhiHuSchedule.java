package com.github.skysilently.dingtalk.collector.msgpusher.schedule;

import com.github.skysilently.dingtalk.collector.msgpusher.module.dto.ZhiHuDTO;
import com.github.skysilently.dingtalk.collector.msgpusher.zhihu.ZhiHuPusher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author keyun
 * @create 2022-11-11 16:09
 **/
@Slf4j
@Component
@EnableScheduling
public class ZhiHuSchedule {

    static {
        log.info("ZhiHuSchedule loaded");
    }

    @Autowired
    ZhiHuPusher pusher;

    @Scheduled(cron = "0 0 10,14,16,18,20 * * 1/5")
    public void push(){
        ZhiHuDTO zhiHuDTO = null;
        try {
            zhiHuDTO = pusher.getZhiHuResp();
        } catch (URISyntaxException | IOException e) {
            log.error("获取知乎热榜失败",e);
            return;
        }
        pusher.dingTalkPush(zhiHuDTO);
        log.info("ZhiHuSchedule push");
    }

}
