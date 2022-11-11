package com.github.skysilently.dingtalk.collector.notificationgetter.qihoo;

import com.github.skysilently.dingtalk.collector.notificationgetter.NotificationUpdater;
import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
@Service
public class QiHooUpdater extends NotificationUpdater {

    private String source = "https://cert.360.cn/feed";

//    private String detailUrl;

//    private Document document;

//    private HashMap<String,String> papers = new HashMap<>();
    private  String report = "report";

    private  String warning = "warning";


    @Override
    public ArrayList<NotificationModel> getNotificationsFromJsoup() throws IOException, ParseException {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        this.getFromSource();
        Elements elements = this.getDocument().select("item");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
//        Date date = new Date(System.currentTimeMillis());
        for (Element element :
                elements) {
            if (element.select("title").text().contains("每日安全简报")) {
//                跳过每日安全简报
                continue;
            }
            AddtoNotificationModelArrayList(notificationModelArrayList, simpleDateFormat, element);
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

    public ArrayList<NotificationModel> getNotificationsFromJsoup(String type) throws IOException, ParseException {
        ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
        this.getFromSource();
        Elements elements = this.getDocument().select("item");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        for (Element element :
                elements) {
            if (element.select("link").text().split("/")[3].equals(type)){
                AddtoNotificationModelArrayList(notificationModelArrayList, simpleDateFormat, element);
            }
        }
        Collections.reverse(notificationModelArrayList);
        return notificationModelArrayList;
    }

    private void AddtoNotificationModelArrayList(ArrayList<NotificationModel> notificationModelArrayList, SimpleDateFormat simpleDateFormat, Element element) throws ParseException, IOException {
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setSource(source);
        notificationModel.setInfoTitle(element.select("title").text());
        notificationModel.setLink(element.select("link").text());
        notificationModel.setDescription(element.select("description").text());
        notificationModel.setPublishDate(new Timestamp(simpleDateFormat.parse(element.select("pubDate").text()).getTime()));
        notificationModel.setType(element.select("link").text().split("/")[3]);
        notificationModel.setDetailImg(getImgSrc(notificationModel.getLink()));
        notificationModelArrayList.add(notificationModel);
    }

    @Override
    public void checkUpdate(){
        ArrayList<NotificationModel> notificationModelArrayListReportFromJsoup =  null;
        ArrayList<NotificationModel> notificationModelArrayListWarningFromJsoup = null;
        NotificationModel notificationModelReportFromDB = null;
        NotificationModel notificationModelWarningFromDB = null;
        try {
            notificationModelArrayListReportFromJsoup = getNotificationsFromJsoup(report);
            notificationModelArrayListWarningFromJsoup = getNotificationsFromJsoup(warning);
            notificationModelReportFromDB = getNotificationsFromDBByTypeAndLimitNum(report,1).get(0);
            notificationModelWarningFromDB = getNotificationsFromDBByTypeAndLimitNum(warning,1).get(0);
        } catch (IOException | ParseException | SQLException e) {
            log.error("get data failed",e);
        }

        while (!notificationModelArrayListReportFromJsoup.isEmpty()){
            if (notificationModelArrayListReportFromJsoup.get(0).getLink().equals(notificationModelReportFromDB.getLink())){
                notificationModelArrayListReportFromJsoup.remove(0);
                break;
            }
            else notificationModelArrayListReportFromJsoup.remove(0);
        }
        if (!notificationModelArrayListReportFromJsoup.isEmpty()){
            try {
                if (notificationService.addNotifications(notificationModelArrayListReportFromJsoup)){
                    log.info("insert new report {}",notificationModelArrayListReportFromJsoup);
                }else log.warn("insert failed {}",notificationModelArrayListReportFromJsoup);
                customDingTalkPusher.push(notificationModelArrayListReportFromJsoup);
            } catch (Exception e) {
                log.error("push custom DingTalk msg failed",e);
            }
        }else log.info("no update exist {}",source);
//
        while (!notificationModelArrayListWarningFromJsoup.isEmpty()){
            if (notificationModelArrayListWarningFromJsoup.get(0).getLink().equals(notificationModelWarningFromDB.getLink())){
                notificationModelArrayListWarningFromJsoup.remove(0);
                break;
            }
            else notificationModelArrayListWarningFromJsoup.remove(0);
        }
        if (!notificationModelArrayListWarningFromJsoup.isEmpty()){
            try {
                if (notificationService.addNotifications(notificationModelArrayListWarningFromJsoup)){
                    log.info("insert new warning {}",notificationModelArrayListWarningFromJsoup);
                }else log.warn("insert failed {}",notificationModelArrayListWarningFromJsoup);
//                暂时取消CVE对比去重功能
//                for (NotificationModel n :
//                        notificationModelArrayListWarningFromJsoup) {
//                    if (CVECheck(n))
//                        notificationModelArrayListWarningFromJsoup.remove(0);
//                }
//                if (!notificationModelArrayListWarningFromJsoup.isEmpty())
                    customDingTalkPusher.push(notificationModelArrayListWarningFromJsoup);
            } catch (Exception e) {
                log.error("push custom DingTalk msg failed",e);
            }
        }else log.info("no update exist {}",source);

    }

    public List<NotificationModel> getNotificationsFromDB(){
        return notificationService.getAllNotifications();
    }

    public String getImgSrc(String detailURL) throws IOException {
        return Jsoup.connect(detailURL).get().select(".news-conent").select("img").first().attr("src");
//        try {
//            return Jsoup.connect(detailURL).get().select(".news-conent").select("img").first().attr("src");
//        } catch (IOException e) {
//            log.error("get qiHoo img failed",e);
//            return "";
//        }
    }


}
