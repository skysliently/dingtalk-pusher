package com.github.skysilently.dingtalk.collector.plugingetter.fastjson;

import com.github.skysilently.dingtalk.module.dto.module.AppDependencyModel;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 试行版数据暂取snyk-Vulnerability DB
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class FastjsonUpdater extends JsoupUpdater {

    private String plugin = "Fastjson_snyk";

    private String url = "https://snyk.io/vuln/search?q=fastjson&type=maven";

    private String dependency = "fastjson";

    private String poluURL = "http://pangu.dev-base.cai-inc.com/polu/api/risk/record/export";
//    private String poluURL = "http://10.201.101.77:10937/polu/api/risk/record/export";

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
        }
        for (int i = 0; i < intNum; i++) {
            SecItemListModel secItemListModel =  new SecItemListModel();
            Elements tds = items.get(i).select("td");
            secItemListModel.setPlugin(this.getPlugin());
            try {
                secItemListModel.setSecInformLink(items.get(i).select("a").attr("abs:href"));
                secItemListModel.setVersionRange(tds.get(1).select("span").get(0).text());
                secItemListModel.setPublishTimeOrigin(tds.get(3).text());
                Document detail = Jsoup.connect(secItemListModel.getSecInformLink()).get();
                secItemListModel.setRisk(detail.select("span[class=vue--badge__text]").get(0).text());
                Elements cveId = detail.select("a[href^=https://cve.mitre.org/cgi-bin/cvename.cgi]");
                if (cveId.size() != 0) {
                    secItemListModel.setCveId(cveId.get(0).text().replace("Open this link in a new tab",""));
                }
                secItemListModel.setDescription(detail.select("h1").text().replace("Open this link in a new tab ",""));
                secItemListModel.setItem((tds.get(0).select("a").text() + "\n" + tds.get(1).select("span").get(0).text() + secItemListModel.getCveId()).replace("Open this link in a new tab",""));
            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                log.error("reading Elements error {}",indexOutOfBoundsException);
            }
            secItemListModelArrayList.add(secItemListModel);
        }
        Collections.reverse(secItemListModelArrayList);
        return secItemListModelArrayList;
    }

    public void checkDependencyVersion(){
        List<AppDependencyModel> appDependencyModelArrayList = appDependencyService.GetAppDependencyByArtifactId(this.getDependency());
        List<SecItemListModel> secItemListModelArrayList = secItemListService.getAllSecItemList(this.getPlugin());
        Collections.reverse(secItemListModelArrayList);
        if (appDependencyModelArrayList == null) {
            log.info("no dependency {} ",this.getDependency());
            return;
        }
        for (AppDependencyModel appDependencyModel :
                appDependencyModelArrayList) {
            for (SecItemListModel secItemListModel :
                    secItemListModelArrayList) {
                if (checkVersion(appDependencyModel.getDependencyJarVersion(),secItemListModel.getVersionRange())) {
                    log.warn("Risk test log warn {}",
                            "risk name: " + secItemListModel.getItem().split("\n")[0] + ";" +
                                    "risk URL: " + secItemListModel.getSecInformLink() + ";" +
                                    "app name: " + appDependencyModel.getAppName() + ";" +
                                    "hit plugin: " + secItemListModel.getPlugin() + ";" +
                                    "app plugin: " + appDependencyModel.getDependencyJarArtifactId() + ";" +
                                    "app plugin version: " + appDependencyModel.getDependencyJarVersion() + "; " +
                                    "plugin risk range: " + secItemListModel.getVersionRange() + ";" +
                                    "date: " + (new Date()).toString());
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Cookie","UM_distinctid=17a9a72bb8e6a2-089db7d012a766-445567-7e900-17a9a72bb8f32e; cna=QCpXGDeFCXYCAT2kLOMAdNcL; JSESSIONID=F56B11D921E718A4FE5D1CD36F89C64A; redirect=; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA2MzMwOTAsImlzcyI6ImtleXVuIiwidXNlcmlkIjoiMTYwNjk2Mzk2OTE0OTI3ODYiLCJ1c2VyTmFtZSI6ImtleXVuIiwibmlja05hbWUiOiLmnJTmtIQiLCJyZWFsTmFtZSI6Iuafr-aYgCIsIm1haWwiOiJrZXl1bkBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDA2NzIiLCJtb2JpbGUiOiIxNTg1ODIyODc2MCIsImRlcGFydG1lbnQiOls0ODM0OTQyOTRdLCJkZXBhcnRtZW50cyI6WyLkv6Hmga_lronlhagiXSwiQWxsUGF0aCI6WyLkv6Hmga_lronlhags56CU5Y-R6YOoLOaUv-mHh-S6keaciemZkOWFrOWPuCJdLCJBbGxQYXRoSWRzIjpbWzQ4MzQ5NDI5NCw0MjY2NDY5LDFdXSwiaXNMZWFkZXIiOmZhbHNlLCJwb3NpdGlvbiI6IuWQjuerryIsImF2YXRhciI6Imh0dHBzOi8vc3RhdGljLWxlZ2FjeS5kaW5ndGFsay5jb20vbWVkaWEvbEFEUEQyNmVObWkxbzFqTTRNemhfMjI1XzIyNC5qcGcifQ.yQfsSQVhkqg5SXopkx1OBHnxaG02xwAZpKoEc40TJ-I; __sso_token__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA2MzMwOTAsImlzcyI6ImtleXVuIiwidXNlcmlkIjoiMTYwNjk2Mzk2OTE0OTI3ODYiLCJ1c2VyTmFtZSI6ImtleXVuIiwibmlja05hbWUiOiLmnJTmtIQiLCJyZWFsTmFtZSI6Iuafr-aYgCIsIm1haWwiOiJrZXl1bkBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDA2NzIiLCJtb2JpbGUiOiIxNTg1ODIyODc2MCIsImRlcGFydG1lbnQiOls0ODM0OTQyOTRdLCJkZXBhcnRtZW50cyI6WyLkv6Hmga_lronlhagiXSwiQWxsUGF0aCI6WyLkv6Hmga_lronlhags56CU5Y-R6YOoLOaUv-mHh-S6keaciemZkOWFrOWPuCJdLCJBbGxQYXRoSWRzIjpbWzQ4MzQ5NDI5NCw0MjY2NDY5LDFdXSwiaXNMZWFkZXIiOmZhbHNlLCJwb3NpdGlvbiI6IuWQjuerryIsImF2YXRhciI6Imh0dHBzOi8vc3RhdGljLWxlZ2FjeS5kaW5ndGFsay5jb20vbWVkaWEvbEFEUEQyNmVObWkxbzFqTTRNemhfMjI1XzIyNC5qcGcifQ.yQfsSQVhkqg5SXopkx1OBHnxaG02xwAZpKoEc40TJ-I; __sso_user__=keyun; __sso_department__=%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8; __sso_realName__=%E6%9F%AF%E6%98%80; __sso_nickName__=%E6%9C%94%E6%B4%84");
                    String requestBody = "{\n" +
                            "  \"assetsType\": \"THIRD_PARTY_COMPONENTS\",\n" +
                            "  \"riskCode\": \"THIRD_PARTY_COMPONENT_RISK\",\n" +
                            "  \"createAt\": "+ System.currentTimeMillis() +",\n" +
                            "  \"remark\": \"ZSec风险信息收集——Fastjson风险探测\",\n" +
                            "  \"pluginRiskName\": \""+ secItemListModel.getItem().split("\n")[0] +"\",\n" +
                            "  \"pluginSource\": \"" + secItemListModel.getSecInformLink() + "\",\n" +
                            "  \"pluginAppName\": \"" + appDependencyModel.getAppName() + "\",\n" +
                            "  \"pluginName\": \"" + appDependencyModel.getDependencyJarArtifactId() + "\",\n" +
                            "  \"pluginVersion\": \"" + appDependencyModel.getDependencyJarVersion() + "\",\n" +
                            "  \"pluginRiskVersion\": \"" + secItemListModel.getVersionRange() + "\"\n" +
                            "}";
                    HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
                    try {
                        ResponseEntity<String> responseEntity = restTemplate
                                .exchange(poluURL, HttpMethod.POST, httpEntity, String.class);
                        log.info("推送结果 {}", responseEntity.getBody());
                    } catch (Exception e) {
                        log.error("推送结果失败 {}", e);
                    }
                    log.warn("Risk test log warn JSON : {}",
                            "{" +
                            "riskName:\"" + secItemListModel.getItem().split("\n")[0] + "\"," +
                            "riskURL:\"" + secItemListModel.getSecInformLink() + "\"," +
                            "appName:\"" + appDependencyModel.getAppName() + "\"," +
                            "hitPlugin:\"" + secItemListModel.getPlugin() + "\"," +
                            "appPlugin:\"" + appDependencyModel.getDependencyJarArtifactId() + "\"," +
                            "appPluginVersion:\"" + appDependencyModel.getDependencyJarVersion() + "\"," +
                            "pluginRiskVersion:\"" + secItemListModel.getVersionRange() + "\"," +
                            "date:\"" + System.currentTimeMillis() + "\"" +
                            "}"
                        );
                }
                else
                {
                    log.info("Risk test log info {}",
                            "plugin: " + appDependencyModel.getAppName() + ";" +
                                    "plugin version: " + appDependencyModel.getDependencyJarVersion() + "; " +
                                    "hit plugin: " + secItemListModel.getPlugin() + " - " + secItemListModel.getVersionRange());
                }
            }
        }
    }

    public boolean checkVersion(String appPluginVersion,String versionRange){
        try {
            int comparableVersionRange = formatVersion(versionRange, 2, 8);
            int comparableAppPluginVersion = formatVersion(appPluginVersion);
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

    public int formatVersion(String Version, int start, int end){
        Version = Version.substring(start,end).replace(".","");
        return Integer.parseInt(String.format("%-4s",Version).replace(" ","0"));
    }

    public int formatVersion(String Version){
        Version = Version.replace(".","");
        return Integer.parseInt(String.format("%-4s",Version).replace(" ","0"));
    }
}
