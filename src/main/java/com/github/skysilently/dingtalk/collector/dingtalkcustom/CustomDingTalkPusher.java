package com.github.skysilently.dingtalk.collector.dingtalkcustom;

import com.github.skysilently.dingtalk.collector.notificationgetter.qihoo.QiHooDailyData;
import com.github.skysilently.dingtalk.module.dto.module.NotificationModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
//@Service
public class CustomDingTalkPusher {

//    群机器人

    private Long timestamp = System.currentTimeMillis();

    public String getSign() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(new String(Base64.encodeBase64(signData)), String.valueOf(StandardCharsets.UTF_8));
    }

    public void push(ArrayList<NotificationModel> notificationModelArrayList) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\n" +
                "    \"keyword\": \"ZSEC\",\n" +
                "    \"msgtype\": \"feedCard\",\n" +
                "    \"feedCard\": {\n" +
                "        \"links\": [";
        Collections.reverse(notificationModelArrayList);
        for (NotificationModel n :
                notificationModelArrayList) {
            requestBody += "{\n" +
                    "                \"title\": \"" + "[ZSEC] " + n.getInfoTitle() + "\", \n" +
                    "                \"messageURL\": \"" + n.getLink() + "\", \n" +
//                    "                \"picURL\": \"" + Jsoup.connect(n.getLink()).get().select(".news-conent").select("img").first().attr("src") + "\"\n" +
                    "                \"picURL\": \"" + n.getDetailImg() + "\"\n" +
                    "            },";
        }
        requestBody += "]\n" +
                "    }\n" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(dingTalkWebHook +
                        "&timestamp=" + this.getTimestamp() +
                        "&sign=" + this.getSign(), HttpMethod.POST, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

//    提示消息
    public void prePush(String msg) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody =
                "{\n" +
                "     \"msgtype\": \"text\",\n" +
                "     \"text\": {\n" +
                "         \"content\": \"" + "[ZSEC] " + msg + "\"\n" +
                "     }" +
                " }";


        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(dingTalkWebHook +
                        "&timestamp=" + this.getTimestamp() +
                        "&sign=" + this.getSign(), HttpMethod.POST, httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

//    360每日安全简报
    public  void  DailySecurityBriefing(Map<String, List<QiHooDailyData>> dailyMap) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        StringBuilder text = new StringBuilder();
        text.append("## [ZSEC]360每日简报  \\n  ");
        for (String key :
                dailyMap.keySet()) {
            text.append("### "+ key +"  \\n  ");
            for (QiHooDailyData qiHooDailyData :
                    dailyMap.get(key)) {
                text.append("> ##### ").append(qiHooDailyData.getReportTitle()).append("  \\n  ");
                text.append("> ").append(qiHooDailyData.getReportLink()).append("  \\n  ");
            }
        }
        String requestBody =
                "{\n" +
                "     \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"[ZSEC] 360每日简报\",\n" +
                "         \"text\": \"" + text.toString().replaceAll("\""," ") + "\"\n" +
                "     }\n" +
                " }";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(dingTalkWebHook +
//                .postForEntity(dingTalkWebHook +
                        "&timestamp=" + this.getTimestamp() +
                        "&sign=" + this.getSign(), HttpMethod.POST, httpEntity, String.class);
        log.info("DingTalk Response: {}",responseEntity.getBody());
    }
}
