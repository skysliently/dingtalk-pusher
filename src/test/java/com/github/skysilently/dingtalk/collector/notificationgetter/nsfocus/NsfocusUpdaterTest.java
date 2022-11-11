package com.github.skysilently.dingtalk.collector.notificationgetter.nsfocus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NsfocusUpdaterTest {

    @Autowired
    private NsfocusUpdater nsfocusUpdater;

    @Test
    public void test() {
//        nsfocusUpdater.init();
//        nsfocusUpdater.checkUpdate();
    }

    @Test
    public void testTime() throws ParseException {
        String time = "2022-03-28T15:57:55+08:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(time);
        System.out.println(date);
    }
}