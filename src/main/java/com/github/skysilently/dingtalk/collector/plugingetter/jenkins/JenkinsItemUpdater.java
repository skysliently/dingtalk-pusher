/*
  只抓取2020年的链接信息
  已替换为列表形式
 */
package com.github.skysilently.dingtalk.collector.plugingetter.jenkins;


import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class JenkinsItemUpdater extends JsoupUpdater {

    private String url = "https://www.jenkins.io/security/advisories/";

    private String plugin = "Jenkins";

//    private final String itemRule;

//    @Autowired
//    private SecItemListService secItemListService;


    //仅2020
//    public ArrayList<SecItemListModel> getAllJenkinsSecItemListModelFromJsoup() throws IOException {
//        return getJenkinsSecItemListModelFromJsoup("all");
//    }

    //仅2020
//    增加2021、2022
    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) throws IOException {
        ArrayList<SecItemListModel> secItemListModelArrayList = new ArrayList<>();
        this.getFromUrl();
        log.info("Plugin : {};\n" +
                "Url :{}", this.getPlugin(), this.getUrl());
        String[] advisories = {
//                "a[href*=https://www.jenkins.io/security/advisory/2021]",
//                "a[href*=https://www.jenkins.io/security/advisory/2020]",
                "a[href*=/security/advisory/2022]",
                "a[href*=/security/advisory/2021]",
                "a[href*=/security/advisory/2020]",
        };
        for (String advisory :
                advisories) {
//            Elements elements = this.getDocument().select("a[href*=https://www.jenkins.io/security/advisory/202]");
            Elements elements = this.getDocument().select(advisory);
            if (elements.size() == 0) {
                continue;
            }
            int itemNum = 0;
            if (itemSize.equals("three")){
                itemNum = 3;
            }else if (itemSize.equals("all")){
                itemNum = elements.size();
            }
            for (int i = 0; i < itemNum; i++) {
                SecItemListModel secItemListModel =  new SecItemListModel();
                secItemListModel.setPlugin(plugin);
                secItemListModel.setSecInformLink(elements.get(i).attr("abs:href"));
                secItemListModel.setItem(elements.get(i).text());
                try {
                    secItemListModel.setPublishTime(new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd")).parse(secItemListModel.getSecInformLink().substring(41,51)).getTime()));
                }catch (Exception e){
                    System.out.println("日期转换失败！");
                    log.error("Transform time fialed use 1970 ; Exception:", e);
                }
                secItemListModel = setJenkinsDetail(secItemListModel);
                secItemListModelArrayList.add(secItemListModel);
            }
        }
        Collections.reverse(secItemListModelArrayList);
        return secItemListModelArrayList;
    }

//    public ArrayList<SecItemListModel> getLast3JenkinsSecItemListModelFromJsoup() throws IOException {
//        return getJenkinsSecItemListModelFromJsoup("three");
//    }

//    public SecItemListModel getLastJenkinsSecItemListModelFromDB(){
//        return secItemListService.getLastSecItemList(this.getPlugin());
//    }

//    public boolean isEmpty(){
//        if (getLastJenkinsSecItemListModelFromDB() == null) {
//            return true;
//        }else return false;
//    }

    /**
     * description取每个漏洞介绍的标题
     * cve取cve编号与Jenkins自定义编号
     * risk取最高的
     * @param secItemListModel
     * @return SecItemListModel
     * @throws IOException
     */
    public SecItemListModel setJenkinsDetail( SecItemListModel secItemListModel) throws IOException {
        Document detailDoc = Jsoup.connect(secItemListModel.getSecInformLink()).get();
        Elements descriptions = detailDoc.select("h3[id*=SECURITY-]");
        StringBuilder description = new StringBuilder();
        for (Element element :
                descriptions) {
            description.append(element.text()).append("\n");
        }
        secItemListModel.setDescription(description.toString());
        Elements cves = detailDoc.select("div[class=col-lg-9] strong");
        StringBuilder cve = new StringBuilder();
        for (int i = 0; i < descriptions.size(); i++) {
            cve.append(cves.get(i).text()).append("\n");
        }
        secItemListModel.setCveId(cve.toString());
        Elements risks = detailDoc.select("a[href*=https://www.first.org/cvss/calculator/]");
        Set<String> riskSet = new HashSet<>();
        for (Element element :
                risks) {
            riskSet.add(element.text());
        }
        boolean find = false;
        String[] riskList = {"Critical","High","Medium","Low"};
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
        return secItemListModel;
    }

//    public void jenkinsInit(){
//        if (isEmpty()) {
//            System.out.println("Dont need init");
//            return;
//        }
//        try {
//            ArrayList<SecItemListModel> secItemListModelArrayList = getAllJenkinsSecItemListModelFromJsoup();
//            if (secItemListService.addSecItems(secItemListModelArrayList) == -1) {
//                System.out.println("插入数据失败！");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public int checkJenkinsUpdate() {
//        ArrayList<SecItemListModel> secItemListModelArrayListFromJsoup = null;
//        try {
//            secItemListModelArrayListFromJsoup = getLast3JenkinsSecItemListModelFromJsoup();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        Collections.reverse(secItemListModelArrayListFromJsoup);
//        SecItemListModel secItemListModel = getLastJenkinsSecItemListModelFromDB();
//        if (secItemListModel == null) {
//            System.out.println("No plugin named " + this.getPlugin());
//            return -1;
//        }
//        while (!secItemListModelArrayListFromJsoup.isEmpty()){
//            if (secItemListModelArrayListFromJsoup.get(0).getItem().equals(secItemListModel.getItem())) {
//                secItemListModelArrayListFromJsoup.remove(0);
//                break;
//            }
//            else secItemListModelArrayListFromJsoup.remove(0);
//        }
//        if (!secItemListModelArrayListFromJsoup.isEmpty()) {
//            return secItemListService.addSecItems(secItemListModelArrayListFromJsoup);
//        }
//        return -1;
//    }


}