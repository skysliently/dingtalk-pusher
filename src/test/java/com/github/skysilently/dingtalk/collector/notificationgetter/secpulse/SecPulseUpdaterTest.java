package com.github.skysilently.dingtalk.collector.notificationgetter.secpulse;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecPulseUpdaterTest {
    @Autowired
    SecPulseUpdater secPulseUpdater;

    @Test
    public void test() {
//        secPulseUpdater.init();
        secPulseUpdater.checkUpdate();
    }

}