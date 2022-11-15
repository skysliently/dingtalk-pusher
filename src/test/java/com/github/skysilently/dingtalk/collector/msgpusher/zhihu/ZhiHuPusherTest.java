package com.github.skysilently.dingtalk.collector.msgpusher.zhihu;

import com.github.skysilently.dingtalk.collector.msgpusher.module.dto.ZhiHuDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ZhiHuPusherTest {

    @Autowired
    ZhiHuPusher pusher;

    @Test
    public void testURL() throws IOException, URISyntaxException {
        ZhiHuDTO zhiHuDTO = pusher.getZhiHuResp();
//        String zhiHuDTO = pusher.get();
//        System.out.println(zhiHuDTO);
//        pusher.dingTalkPush(zhiHuDTO);
        pusher.dingTalkPushTopHub();
    }

}