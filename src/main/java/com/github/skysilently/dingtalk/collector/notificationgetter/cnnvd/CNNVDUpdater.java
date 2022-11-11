package com.github.skysilently.dingtalk.collector.notificationgetter.cnnvd;

import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
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

/**
 * 抓取CNNVD漏洞通告
 * @author keyun
 * @create 2022-06-10 17:12
 **/
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class CNNVDUpdater extends NotificationUpdater {

    private String source = "http://www.cnnvd.org.cn/web/cnnvdnotice/querylist.tag";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException, ParseException {
        this.getFromSource();
        Elements elements = this.getDocument().select("li").select(".fl");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        for (Element element :
                elements) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle(element.select("a").text());
            notificationModel.setType("warning");
            notificationModel.setLink(element.select("a").attr("abs:href"));
            notificationModel.setDescription("CNNVD漏洞通报");
            notificationModel.setPublishDate(new Timestamp(simpleDateFormat.parse(element.nextElementSibling().text()).getTime()));
            notificationModel.setDetailImg("http://www.cnnvd.org.cn/web/images/logo.png");
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }
}
