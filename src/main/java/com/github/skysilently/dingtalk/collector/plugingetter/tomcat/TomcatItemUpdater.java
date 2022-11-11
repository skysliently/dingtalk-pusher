package com.github.skysilently.dingtalk.collector.plugingetter.tomcat;

import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class TomcatItemUpdater extends JsoupUpdater {

    private String plugin = "Tomcat";

    private String url = "http://tomcat.apache.org/security.html";

//    @Autowired
//    SecItemListService secItemListService;

//    public void tomcatInit(){
//        if (!isEmpty()){
//            System.out.println(this.getPlugin() + " dont need init");
//            return;
//        }
//        try {
//            ArrayList<SecItemListModel> secItemListModelArrayList = getAllTomcatSecItemListModelFromJsoup();
//            if (secItemListService.addSecItems(secItemListModelArrayList) == -1) {
//                System.out.println("插入数据失败！");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void checkTomcatUpdate() {
//        ArrayList<SecItemListModel> secItemListModelArrayListFromJsoup = null;
//        try {
//            secItemListModelArrayListFromJsoup = getLast3TomcatSecItemListModelFromJsoup();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        Collections.reverse(secItemListModelArrayListFromJsoup);
//        SecItemListModel secItemListModel = getLastSecItemListModelFromDB();
//        if (secItemListModel == null) {
//            System.out.println("No plugin named " + this.getPlugin());
//            return;
//        }
//        while (!secItemListModelArrayListFromJsoup.isEmpty()){
//            if (secItemListModelArrayListFromJsoup.get(0).getItem().equals(secItemListModel.getItem())) {
//                secItemListModelArrayListFromJsoup.remove(0);
//                break;
//            }
//            else secItemListModelArrayListFromJsoup.remove(0);
//        }
//        if (!secItemListModelArrayListFromJsoup.isEmpty()) {
//            secItemListService.addSecItems(secItemListModelArrayListFromJsoup);
//        }
////        return -1;
//    }

//    public ArrayList<SecItemListModel> getAllTomcatSecItemListModelFromJsoup() throws IOException {
//        return getTomcatSecItemListModelFromJsoup("all");
//    }
//
//    public ArrayList<SecItemListModel> getLast3TomcatSecItemListModelFromJsoup() throws IOException {
//        return getTomcatSecItemListModelFromJsoup("three");
//    }

//    public boolean isEmpty(){
//        if (getLastTomcatSecItemListModelFromDB() == null) {
//            return true;
//        }else return false;
//    }

    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) {
        ArrayList<SecItemListModel> secItemListModelArrayList = new ArrayList<>();
        this.getFromUrl();
        log.info("Plugin : {};\n" +
                "Url :{}", this.getPlugin(), this.getUrl());
        Elements items = this.getDocument().select("h3[id^=Fixed_in_Apache_Tomcat_]");
        int intNum = 0;
        if (itemSize.equals("three")) {
            intNum = 3;
        }else if (itemSize.equals("all")) {
            intNum = items.size();
        }
        for (int i = 0; i < intNum; i++) {
            SecItemListModel secItemListModel =  new SecItemListModel();
            secItemListModel.setPlugin(this.getPlugin());
            secItemListModel.setSecInformLink(this.getUrl() + "#" + items.get(i).id());
            secItemListModel.setItem(items.get(i).id());
            Elements descriptions = items.get(i).nextElementSibling().select("strong");
            Set<String> riskSet = new HashSet<>();
            StringBuilder description = new StringBuilder();
            for (Element desc:
                 descriptions) {
                description.append(desc.text()).append("\n");
                riskSet.add((desc.text().split(":", 2))[0]);
            }
            secItemListModel.setDescription(description.toString());
            boolean find = false;
            String[] riskList = {"Critical","High","Important","Moderate","Low"};
            for (String risk :
                    riskList) {
                if (!find) {
                    for (String string :
                            riskSet) {
                        if (string.equals(risk)) {
                            find = true;
                            secItemListModel.setRisk(risk);
                        }
                    }
                }
            }
            Elements cveIds = items.get(i).nextElementSibling().select("a[href^=http://cve.mitre.org/]");
            StringBuilder cveId = new StringBuilder();
            for (Element cve:
                    cveIds) {
                cveId.append(cve.text()).append("\n");
            }
            secItemListModel.setCveId(cveId.toString());
            secItemListModel.setPublishTimeOrigin(items.get(i).select("span").text());
            try {
                secItemListModel.setPublishTime(new Date((new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)).parse(items.get(i).select("span").text()).getTime()));
            } catch (ParseException e) {
                secItemListModel.setPublishTime(new Date(0));
                log.error("Transform time fialed use 1970 ; Exception: {}", e);
//                e.printStackTrace();
            }
            secItemListModelArrayList.add(secItemListModel);
        }
        Collections.reverse(secItemListModelArrayList);
        return  secItemListModelArrayList;
    }

//    public SecItemListModel getLastTomcatSecItemListModelFromDB(){
//        return secItemListService.getLastSecItemList(this.getPlugin());
//    }

//    public ArrayList<SecItemListModel> getAllJenkinsSecItemListModelFromDB(){
//        return secItemListService.getAllSecItemList(this.getPlugin());
//    }

//    public SecItemListModel dbTest(){
//        return secItemListService.getLastSecItemList("tt");
//    }

}
