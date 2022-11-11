package com.github.skysilently.dingtalk.collector.notificationgetter;

import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import com.github.skysilently.dingtalk.service.api.NotificationService;
import com.github.skysilently.dingtalk.collector.dingtalkcustom.CustomDingTalkPusher;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
@Service
public abstract class NotificationUpdater {

    private String source;

    private Document document;

    @Autowired
    protected NotificationService notificationService;

//    @Autowired
//    CustomDingTalkPusher customDingTalkPusher;
    protected CustomDingTalkPusher customDingTalkPusher = new CustomDingTalkPusher();

    public void getFromSource() throws IOException {
        try {
            this.setDocument(Jsoup.connect(this.getSource()).get());
        } catch (Exception e) {
            log.error("Jsoup get resource failed:",e);
        }
    }

    public void init(){
        ArrayList<NotificationModel> notificationModelArrayList;
        try {
            notificationModelArrayList =getNotificationsFromJsoup();
        } catch (Exception e) {
            log.error("get notifications from jsoup failed:", e);
            return;
        }
        try {
            notificationService.addNotifications(notificationModelArrayList);
        } catch (Exception e) {
            log.error("SQL error:", e);
        }
    }

    public void checkUpdate(){
        ArrayList<NotificationModel> notificationModelArrayListFromJsoup;
        NotificationModel notificationModelFromDB;
        try {
            notificationModelArrayListFromJsoup = getNotificationsFromJsoup();
            notificationModelFromDB = getNotificationsFromDBByTypeAndLimitNum("warning",1).get(0);
        } catch (IOException | ParseException | SQLException e) {
            log.error("get notificationModel data failed:",e);
            return;
        }

        while (true){
            assert notificationModelArrayListFromJsoup != null;
            if (notificationModelArrayListFromJsoup.isEmpty()) break;
            assert notificationModelFromDB != null;
            if (notificationModelArrayListFromJsoup.get(0).getLink().equals(notificationModelFromDB.getLink())){
                notificationModelArrayListFromJsoup.remove(0);
                break;
            }
            else notificationModelArrayListFromJsoup.remove(0);
        }
        if (!notificationModelArrayListFromJsoup.isEmpty()){
            try {
                if (notificationService.addNotifications(notificationModelArrayListFromJsoup)) {
                    log.info("insert new warning {}",notificationModelArrayListFromJsoup);
                }else log.warn("insert failed {}",notificationModelArrayListFromJsoup);
//                暂时取消CVE对比去重功能
//                for (NotificationModel n :
//                        notificationModelArrayListFromJsoup) {
//                    if (CVECheck(n))
//                        notificationModelArrayListFromJsoup.remove(0);
//                }
//                if (!notificationModelArrayListFromJsoup.isEmpty())
                    customDingTalkPusher.push(notificationModelArrayListFromJsoup);
            } catch (NoSuchAlgorithmException | IOException | InvalidKeyException e) {
                log.error("insert new warning failed:", e);
            }
        }else log.info("no update exist {}",this.getSource());
    }

    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException, ParseException {
        return null;
    }

    public List<NotificationModel> getNotificationsFromDBByTypeAndLimitNum(String type, int limitNum) throws SQLException {
        return notificationService.getNotificationsBySourceAndTypeAndLimitNum(this.getSource(),type,limitNum);
    }

    public List<NotificationModel> getWarningNotificationsFromDBWithoutSelfLimit2Days(String source) {
        return notificationService.getWarningNotificationsWithoutSelfLimit2Days(source);
    }


    public boolean CVECheck(NotificationModel notificationModel) {
        Pattern pattern = Pattern.compile("CVE-\\d{4}-\\d{4,5}");
        String title = notificationModel.getInfoTitle();
        Matcher matcher = pattern.matcher(title);
        if (!matcher.find()) {
            return false;
        }
        String cve = matcher.group(1);
        List<NotificationModel> notificationModelArrayList = this.getWarningNotificationsFromDBWithoutSelfLimit2Days(this.getSource());
        for (NotificationModel nm:
             notificationModelArrayList) {
            Pattern pattern2 = Pattern.compile("CVE-\\d{4}-\\d{4,5}");
            String title2 = nm.getInfoTitle();
            Matcher matcher2 = pattern.matcher(title2);
            if (!matcher2.find()) {
                break;
            }
            if (matcher2.group(1).equals(cve)) {
                return true;
            }
        }
        return false;
    }

//    public boolean TextCheck(NotificationModel nm1, NotificationModel nm2){}
}
