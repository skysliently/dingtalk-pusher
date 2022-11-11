package com.github.skysilently.dingtalk.collector.plugingetter.xstream;

import com.github.skysilently.dingtalk.collector.common.PoluUtil;
import com.github.skysilently.dingtalk.module.dto.module.AppDependencyModel;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class XStreamItemUpdater extends JsoupUpdater {

    private String plugin = "XStream_snyk";

    private String url = "https://security.snyk.io/search?type=maven&q=xstream";

    private String dependency = "xstream";

    PoluUtil poluUtil = new PoluUtil();

    @Override
    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) throws IOException {
        ArrayList<SecItemListModel> secItemListModelArrayList = new ArrayList<>();
        this.getFromUrl();
        log.info("Plugin : {};\n" +
                "Url :{}", this.getPlugin(), this.getUrl());
        Elements items = this.getDocument().select("tbody > tr");
        int intNum = 0;
        if (itemSize.equals("three")) {
            intNum = Math.min(3, items.size());
        }else if (itemSize.equals("all")) {
            intNum = items.size();
//            Collections.reverse(items);
        }
        for (int i = 0; i < intNum; i++) {
            SecItemListModel secItemListModel =  new SecItemListModel();
            Elements tds = items.get(i).select("td");
            secItemListModel.setPlugin(this.getPlugin());
            secItemListModel.setSecInformLink(items.get(i).select("a").attr("abs:href"));
            secItemListModel.setVersionRange(tds.get(1).select("span").get(1).text());
            secItemListModel.setPublishTimeOrigin(tds.get(3).text());
            Document detail = Jsoup.connect(secItemListModel.getSecInformLink()).get();
            secItemListModel.setRisk(detail.select("span[class=vue--badge__text]").get(0).text());
            Elements cveId = detail.select("a[href^=https://cve.mitre.org/cgi-bin/cvename.cgi]");
            if (cveId.size() != 0) {
//                secItemListModel.setCveId(cveId.get(1).text().split(" ")[0]);
                secItemListModel.setCveId(cveId.get(0).text().split(" ")[0]);
//                secItemListModel.setCveId(detail.select("a[href^=https://cve.mitre.org/cgi-bin/cvename.cgi]").get(1).text().split(" ")[0]);
            }
            secItemListModel.setDescription(detail.select("h1").text().replace("Open this link in a new tab ",""));
            secItemListModel.setItem(tds.get(0).select("a").text() + "\n" + tds.get(1).select("span").get(1).text() + secItemListModel.getCveId());
            secItemListModelArrayList.add(secItemListModel);
        }
        Collections.reverse(secItemListModelArrayList);
        return secItemListModelArrayList;
    }

    public void checkDependencyVersion() {
        List<AppDependencyModel> appDependencyModelArrayList = appDependencyService.GetAppDependencyByArtifactId(this.getDependency());
        List<SecItemListModel> secItemListModelArrayList = secItemListService.getAllSecItemList(this.getPlugin());
        SecItemListModel secItemListModel = secItemListModelArrayList.get(0);
        if (appDependencyModelArrayList == null) {
            log.info("no dependency {} ",this.getDependency());
            return;
        }
        for (AppDependencyModel appDependencyModel :
                appDependencyModelArrayList) {
            if (checkVersion(appDependencyModel.getDependencyJarVersion(),secItemListModel.getVersionRange())) {
                poluUtil.pushResultToPolu(secItemListModel,appDependencyModel,dependency);
            }
        }
    }

    public boolean checkVersion(String appPluginVersion,String versionRange){
        try {
            int comparableVersionRange = poluUtil.formatVersion(versionRange, 2, 8);
            int comparableAppPluginVersion = poluUtil.formatVersion(appPluginVersion);
            if (comparableAppPluginVersion < comparableVersionRange) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            log.error("版本对比错误{}",e);
            return false;
        }
    }
}
