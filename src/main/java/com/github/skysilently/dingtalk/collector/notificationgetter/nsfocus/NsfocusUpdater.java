package com.github.skysilently.dingtalk.collector.notificationgetter.nsfocus;

import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class NsfocusUpdater extends NotificationUpdater {

    private String source = "http://blog.nsfocus.net/category/threat-alert/";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException, ParseException {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        this.getFromSource();
        Elements elements = this.getDocument().select("#main").select(".blog-article");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ENGLISH);
        for (Element element :
                elements) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle(element.select(".blog-title").text());
            notificationModel.setType("warning");
            notificationModel.setLink(element.select(".blog-title").select("a").attr("abs:href"));
            notificationModel.setPublishDate(new Timestamp(simpleDateFormat.parse(element.select("time").attr("datetime")).getTime()));
            notificationModel.setDetailImg(element.select(".blog-thumb").select("img").attr("src"));
            notificationModel.setDescription(element.select("p").get(0).text());
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

}
