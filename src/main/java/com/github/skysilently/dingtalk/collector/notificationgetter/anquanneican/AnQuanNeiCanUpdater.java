package com.github.skysilently.dingtalk.collector.notificationgetter.anquanneican;

import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author keyun
 * @create 2022-11-04 14:23
 **/
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class AnQuanNeiCanUpdater extends NotificationUpdater {

    private String source = "https://www.secrss.com/";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() {
        try {
            this.getFromSource();
        } catch (IOException e) {
            log.error("获取HTTP数据错误",e);
        }
        Elements elements = this.getDocument().select("main").select(".list-item");
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        for (Element element : elements) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle("[安全内参]" + element.select(".title").text());
            notificationModel.setType("warning");
            notificationModel.setLink(element.select(".title").select("a").attr("abs:href"));
            notificationModel.setDetailImg(element.select("img").attr("src"));
            notificationModel.setDescription(element.select(".intro").text());
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

}
