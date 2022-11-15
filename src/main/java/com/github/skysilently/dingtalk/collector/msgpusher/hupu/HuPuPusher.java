package com.github.skysilently.dingtalk.collector.msgpusher.hupu;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author keyun
 * @create 2022-11-15 10:43
 **/
@Slf4j
@Component
public class HuPuPusher {

    private final String source = "https://bbs.hupu.com/all-gambia";

    private final String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8";

    private final String acceptEncoding = "gzip, deflate, br";

    private final String acceptLanguage = "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2";

    private final String cacheControl = "no-cache";

    private final String secFetchDest = "document";

    private final String secFetchMode = "navigate";

    private final String secFetchSite = "same-origin";

    private final String secFetchUser = "?1";

    private final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:106.0) Gecko/20100101 Firefox/106.0";

    public Map<String, String> getHuPuFromJsoup() throws IOException {
        Document document = Jsoup
                .connect(source)
                .userAgent(userAgent)
                .header(HttpHeaders.ACCEPT,accept)
                .header(HttpHeaders.ACCEPT_ENCODING,acceptEncoding)
                .header(HttpHeaders.ACCEPT_LANGUAGE,acceptLanguage)
                .header(HttpHeaders.CACHE_CONTROL,cacheControl)
                .header("Sec-Fetch-Dest",secFetchDest)
                .header("Sec-Fetch-Mode",secFetchMode)
                .header("Sec-Fetch-Site",secFetchSite)
                .header("Sec-Fetch-User",secFetchUser)
                .get();
//        String
        Elements elements = document.select(".list-item-wrap");
        Map<String, String> resultMap = new HashMap<>();
        for (Element element :
                elements) {
            try {
                Element tInfo = element.select(".t-info").select("a").get(0);
                String tag  = element.select(".t-label").select("a").get(0).text();
                tag = "[" + tag + "]";
                resultMap.put(tag + tInfo.text(),tInfo.attr("abs:href"));
            } catch (Exception e) {
                log.warn("数据读取异常",e);
            }
        }
        return resultMap;
    }

}
