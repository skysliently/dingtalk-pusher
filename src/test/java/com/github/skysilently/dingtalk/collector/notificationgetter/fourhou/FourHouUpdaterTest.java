package com.github.skysilently.dingtalk.collector.notificationgetter.fourhou;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FourHouUpdaterTest {
    @Autowired
    FourHouUpdater updater;

    @Test
    public void test() {
//        updater.init();
        updater.checkUpdate();
    }

}