package com.github.skysilently.dingtalk.collector.msgpusher.zhihu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 获取知乎数据
 * @author keyun
 * @create 2022-11-09 11:48
 **/
@Slf4j
@Component
public class ZhiHuPusher {

    private String source = "https://www.zhihu.com/api/v4/creators/rank/hot?domain=0&period=day";

    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

    private String accept = "*/*";

    private String acceptEncoding = "gzip, deflate, br";

    private String acceptLanguage = "zh-CN,zh;q=0.9,zh-TW;q=0.8";

    private String referer = "https://www.zhihu.com/knowledge-plan/hot-question/hot/0/day";

    private String secChUa = "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"";

    private String secChUaMobile = "?0";

    private String secChUaPlatform ="\"macOS\"";

    private String secFetchDest = "empty";

    private String secFetchMode = "cors";

    private String secFetchSite = "same-origin";


}
