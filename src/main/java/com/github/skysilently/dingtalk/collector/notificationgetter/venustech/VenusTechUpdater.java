package com.github.skysilently.dingtalk.collector.notificationgetter.venustech;

import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

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
@Service
public class VenusTechUpdater extends NotificationUpdater {
    private String source = "https://www.venustech.com.cn/new_type/aqtg/";

    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException, ParseException {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        this.getFromSource();
        Element element = this.getDocument().select(".safetyList").first();
        Elements elements = element.select(".safetyItem");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        for (Element e :
                elements) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setSource(source);
            notificationModel.setInfoTitle(e.select("a").text());
            notificationModel.setType("warning");
            notificationModel.setLink(e.select("a").attr("abs:href"));
            notificationModel.setPublishDate(new Timestamp(simpleDateFormat.parse(e.select("span").text()).getTime()));
            notificationModel.setDetailImg(Jsoup.connect(notificationModel.getLink()).get().select("img[title=image.png]").first().attr("abs:src"));
            notificationModelArrayList.add(notificationModel);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

//    @Override
//    public void checkUpdate() {
//        ArrayList<NotificationModel> notificationModelArrayListFromJsoup =  null;
//        NotificationModel notificationModelFromDB = null;
//        try {
//            notificationModelArrayListFromJsoup = getNotificationsFromJsoup();
//            notificationModelFromDB = getNotificationsFromDBByTypeAndLimitNum("warning",1).get(0);
//        } catch (IOException | ParseException | SQLException e) {
//            LOGGER.error(e);
//            e.printStackTrace();
//        }
//
//        while (true){
//            assert notificationModelArrayListFromJsoup != null;
//            if (notificationModelArrayListFromJsoup.isEmpty()) break;
//            assert notificationModelFromDB != null;
//            if (notificationModelArrayListFromJsoup.get(0).getLink().equals(notificationModelFromDB.getLink())){
//                notificationModelArrayListFromJsoup.remove(0);
//                break;
//            }
//            else notificationModelArrayListFromJsoup.remove(0);
//        }
//        if (!notificationModelArrayListFromJsoup.isEmpty()){
//            try {
//                if (notificationService.addNotifications(notificationModelArrayListFromJsoup)) {
//                    LOGGER.info("insert new warning {}",notificationModelArrayListFromJsoup);
//                }else LOGGER.warn("insert failed {}",notificationModelArrayListFromJsoup);
//                customDingTalkPusher.push(notificationModelArrayListFromJsoup);
//            } catch (SQLException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
//                LOGGER.error(e);
//                e.printStackTrace();
//            }
//        }else LOGGER.info("no update exist {}",source);
//    }
}
