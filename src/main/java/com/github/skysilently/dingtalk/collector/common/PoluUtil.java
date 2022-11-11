package com.github.skysilently.dingtalk.collector.common;

import com.github.skysilently.dingtalk.module.dto.module.AppDependencyModel;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
public class PoluUtil {


    public void pushResultToPolu(SecItemListModel secItemListModel, AppDependencyModel appDependencyModel, String remarkName) {
        log.info("Risk find log {}",
                "risk name: " + secItemListModel.getItem().split("\n")[0] + ";" +
                        "risk URL: " + secItemListModel.getSecInformLink() + ";" +
                        "app name: " + appDependencyModel.getAppName() + ";" +
                        "hit plugin: " + secItemListModel.getPlugin() + ";" +
                        "app plugin: " + appDependencyModel.getDependencyJarArtifactId() + ";" +
                        "app plugin version: " + appDependencyModel.getDependencyJarVersion() + "; " +
                        "plugin risk range: " + secItemListModel.getVersionRange() + ";" +
                        "date: " + (new Date()).toString());
//        推送结果
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", "UM_distinctid=17a9a72bb8e6a2-089db7d012a766-445567-7e900-17a9a72bb8f32e; cna=QCpXGDeFCXYCAT2kLOMAdNcL; JSESSIONID=F56B11D921E718A4FE5D1CD36F89C64A; redirect=; token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA2MzMwOTAsImlzcyI6ImtleXVuIiwidXNlcmlkIjoiMTYwNjk2Mzk2OTE0OTI3ODYiLCJ1c2VyTmFtZSI6ImtleXVuIiwibmlja05hbWUiOiLmnJTmtIQiLCJyZWFsTmFtZSI6Iuafr-aYgCIsIm1haWwiOiJrZXl1bkBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDA2NzIiLCJtb2JpbGUiOiIxNTg1ODIyODc2MCIsImRlcGFydG1lbnQiOls0ODM0OTQyOTRdLCJkZXBhcnRtZW50cyI6WyLkv6Hmga_lronlhagiXSwiQWxsUGF0aCI6WyLkv6Hmga_lronlhags56CU5Y-R6YOoLOaUv-mHh-S6keaciemZkOWFrOWPuCJdLCJBbGxQYXRoSWRzIjpbWzQ4MzQ5NDI5NCw0MjY2NDY5LDFdXSwiaXNMZWFkZXIiOmZhbHNlLCJwb3NpdGlvbiI6IuWQjuerryIsImF2YXRhciI6Imh0dHBzOi8vc3RhdGljLWxlZ2FjeS5kaW5ndGFsay5jb20vbWVkaWEvbEFEUEQyNmVObWkxbzFqTTRNemhfMjI1XzIyNC5qcGcifQ.yQfsSQVhkqg5SXopkx1OBHnxaG02xwAZpKoEc40TJ-I; __sso_token__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzA2MzMwOTAsImlzcyI6ImtleXVuIiwidXNlcmlkIjoiMTYwNjk2Mzk2OTE0OTI3ODYiLCJ1c2VyTmFtZSI6ImtleXVuIiwibmlja05hbWUiOiLmnJTmtIQiLCJyZWFsTmFtZSI6Iuafr-aYgCIsIm1haWwiOiJrZXl1bkBjYWktaW5jLmNvbSIsImVtcGxveWVlSWQiOiJDMDA2NzIiLCJtb2JpbGUiOiIxNTg1ODIyODc2MCIsImRlcGFydG1lbnQiOls0ODM0OTQyOTRdLCJkZXBhcnRtZW50cyI6WyLkv6Hmga_lronlhagiXSwiQWxsUGF0aCI6WyLkv6Hmga_lronlhags56CU5Y-R6YOoLOaUv-mHh-S6keaciemZkOWFrOWPuCJdLCJBbGxQYXRoSWRzIjpbWzQ4MzQ5NDI5NCw0MjY2NDY5LDFdXSwiaXNMZWFkZXIiOmZhbHNlLCJwb3NpdGlvbiI6IuWQjuerryIsImF2YXRhciI6Imh0dHBzOi8vc3RhdGljLWxlZ2FjeS5kaW5ndGFsay5jb20vbWVkaWEvbEFEUEQyNmVObWkxbzFqTTRNemhfMjI1XzIyNC5qcGcifQ.yQfsSQVhkqg5SXopkx1OBHnxaG02xwAZpKoEc40TJ-I; __sso_user__=keyun; __sso_department__=%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8; __sso_realName__=%E6%9F%AF%E6%98%80; __sso_nickName__=%E6%9C%94%E6%B4%84");
        String requestBody = "{" +
                "  \"assetsType\": \"THIRD_PARTY_COMPONENTS\"," +
                "  \"riskCode\": \"THIRD_PARTY_COMPONENT_RISK\"," +
                "  \"createAt\": " + System.currentTimeMillis() + "," +
                "  \"remark\": \"ZSec风险信息收集——" + remarkName +"风险探测\"," +
                "  \"pluginRiskName\": \"" + secItemListModel.getItem().split("\n")[0] + "\"," +
                "  \"pluginSource\": \"" + secItemListModel.getSecInformLink() + "\"," +
                "  \"pluginAppName\": \"" + appDependencyModel.getAppName() + "\"," +
                "  \"pluginName\": \"" + appDependencyModel.getDependencyJarArtifactId() + "\"," +
                "  \"pluginVersion\": \"" + appDependencyModel.getDependencyJarVersion() + "\"," +
                "  \"pluginRiskVersion\": \"" + secItemListModel.getVersionRange() + "\"" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate
                    .exchange(URLUtil.poluURL, HttpMethod.POST, httpEntity, String.class);
            log.info("推送结果 {}", responseEntity.getBody());
        } catch (Exception e) {
            log.error("推送结果失败 {}", e);
        }
//                    System.out.println(responseEntity.getBody());
        log.info("Risk find log JSON : {}",
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

    public int formatVersion(String version, int start, int end){
        version = version.substring(start,end).replace(".","");
        return Integer.parseInt(String.format("%-4s",version).replace(" ","0"));
    }

    public int formatVersion(String version){
        version = version.replace(".","");
        return Integer.parseInt(String.format("%-4s",version).replace(" ","0"));
    }
}