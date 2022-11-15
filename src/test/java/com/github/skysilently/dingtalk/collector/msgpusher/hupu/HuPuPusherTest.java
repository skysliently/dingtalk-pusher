package com.github.skysilently.dingtalk.collector.msgpusher.hupu;

import com.github.skysilently.dingtalk.collector.msgpusher.DingTalkPusher;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class HuPuPusherTest {

    @Autowired
    HuPuPusher pusher;

    @Test
    public void test() {
        try {
            Map<String, String> resultMap = pusher.getHuPuFromJsoup();
//            DingTalkPusher.dingTalkPush("虎扑热榜",resultMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}