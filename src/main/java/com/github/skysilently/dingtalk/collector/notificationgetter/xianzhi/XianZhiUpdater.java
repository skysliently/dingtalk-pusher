package com.github.skysilently.dingtalk.collector.notificationgetter.xianzhi;

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

/**
 * @author keyun
 * @create 2022-11-04 15:03
 **/
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class XianZhiUpdater extends NotificationUpdater {

    private String source = "https://xz.aliyun.com/feed";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() {
        try {
            this.getFromSource();
        } catch (IOException e) {
            log.error("获取HTTP数据错误",e);
        }
        Elements elements = this.getDocument().select("entry");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        for (Element element:elements) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle("[先知社区]" + element.select("title").text());
            notificationModel.setType("warning");
            notificationModel.setLink(element.select("id").text());
//            notificationModel.setDescription(element.select("description").text());
            try {
                notificationModel.setPublishDate(new Timestamp(simpleDateFormat.parse(element.select("published").text()).getTime()));
            } catch (ParseException e) {
                log.error("格式化日期错误",e);
            }
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

}
