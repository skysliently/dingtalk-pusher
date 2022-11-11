package com.github.skysilently.dingtalk.collector.notificationgetter.anquanke;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class AnQuanKeUpdaterTest {

    @Autowired
    AnQuanKeUpdater anQuanKeUpdater;

    @Test
    public void test() {
//        anQuanKeUpdater.init();
        anQuanKeUpdater.checkUpdate();
    }

}