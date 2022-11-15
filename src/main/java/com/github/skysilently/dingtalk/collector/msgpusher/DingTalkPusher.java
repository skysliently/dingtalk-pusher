package com.github.skysilently.dingtalk.collector.msgpusher;

import com.github.skysilently.dingtalk.collector.dingtalkcustom.CustomDingTalkPusher;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author keyun
 * @create 2022-11-15 11:13
 **/
@Slf4j
public class DingTalkPusher {

    public static void dingTalkPush(String title,Map<String,String> resultMap) {
        StringBuilder text = new StringBuilder();
        text.append("## [FishMan] " + title + "  \\n  ");
        for (String desc :
                resultMap.keySet()) {
            text.append("> ##### ").append(desc).append("  \\n  ");
            text.append("> ").append(resultMap.get(desc)).append("  \\n  ");
        }
        String requestBody =
                "{\n" +
                        "     \"msgtype\": \"markdown\",\n" +
                        "     \"markdown\": {\n" +
                        "         \"title\":\"[FishMan] " + title + "\",\n" +
                        "         \"text\": \"" + text.toString().replaceAll("\""," ") + "\"\n" +
                        "     }\n" +
                        " }";
        CustomDingTalkPusher customDingTalkPusher = new CustomDingTalkPusher();
        try {
            customDingTalkPusher.pushText(requestBody);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.error("钉钉发送失败",e);
        }
    }

}
