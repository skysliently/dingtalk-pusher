package com.github.skysilently.dingtalk.collector.notificationgetter.tencent;

import com.github.skysilently.dingtalk.collector.module.Item;
import com.github.skysilently.dingtalk.collector.module.TencentResp;
import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.google.gson.Gson;
import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Service
public class TencentUpdater extends NotificationUpdater {
    private String source = "https://s.tencent.com/research?page=1&id=18";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        this.getFromSource();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Gson gson = new Gson();
        TencentResp tencentResp = gson.fromJson(this.getDocument().select("#__NEXT_DATA__").get(0).html(), TencentResp.class);
        for (Item item:
             tencentResp.getProps().getPageProps().getAllData().getData().getItems()) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle(item.getTitle());
            notificationModel.setDescription(item.getDescription());
            notificationModel.setType("warning");
            notificationModel.setLink("https://s.tencent.com/research/report/" + item.getId());
            notificationModel.setPublishDate(new Timestamp(System.currentTimeMillis()));
            notificationModel.setDetailImg(item.getThumb());
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }
}