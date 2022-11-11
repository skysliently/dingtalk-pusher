/*
  360-CERT每日安全简报
  Daily Security Briefing
 */
package com.github.skysilently.dingtalk.collector.notificationgetter.qihoo;

import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Service
public class QiHooDailyUpdater extends NotificationUpdater {

    private String source = "https://cert.360.cn/daily";

    public void pushDaily(){
        try {
            this.getFromSource();
        } catch (IOException e) {
            log.error("获取数据错误", e);
            e.printStackTrace();
        }
//        获取每日推送版块元素集合
        Elements elements = this.getDocument().select(".report-block");
        Map<String, List<QiHooDailyData>> dailyMap = new HashMap<>();
//        遍历其中数据
        for (Element element : elements) {
            Elements reportItems = element.select(".report-item");
            List<QiHooDailyData> qiHooDailyDataList = new ArrayList<>();
            for (Element reportItem :
                    reportItems) {
                QiHooDailyData qiHooDailyData = new QiHooDailyData();
                qiHooDailyData.setReportTitle(reportItem.select(".report-title").text());
                qiHooDailyData.setReportLink(reportItem.select(".report-link").text());
                qiHooDailyDataList.add(qiHooDailyData);
            }
//            装入数据
            dailyMap.put(element.select(".block-title").text(),qiHooDailyDataList);
        }
        if (dailyMap.keySet().size() == 0) {
            log.info("360每日简报无内容");
            return;
        }
        try {
            customDingTalkPusher.DailySecurityBriefing(dailyMap);
            log.info("360每日简报已推送");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.error("推送失败",e);
        }
    }

}
