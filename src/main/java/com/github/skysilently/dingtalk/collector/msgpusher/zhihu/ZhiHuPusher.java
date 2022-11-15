package com.github.skysilently.dingtalk.collector.msgpusher.zhihu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.skysilently.dingtalk.collector.dingtalkcustom.CustomDingTalkPusher;
import com.github.skysilently.dingtalk.collector.msgpusher.module.dto.ZhiHuCookieDTO;
import com.github.skysilently.dingtalk.collector.msgpusher.module.dto.ZhiHuDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 获取知乎数据
 * @author keyun
 * @create 2022-11-09 11:48
 **/
@Slf4j
@Component
public class ZhiHuPusher {

    private static final ObjectMapper objectMapper = new ObjectMapper();

//    private final String source = "https://www.zhihu.com/api/v4/creators/rank/hot?domain=0&period=day";
    private final String source = "https://www.zhihu.com/api/v4/creators/rank/hot?domain=0&period=hour";
    private final String thirdSource = "https://tophub.today/n/mproPpoq6O"; // 第三方，登录后首页推送

    private final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

    private final String accept = "*/*";

    private final String acceptEncoding = "gzip, deflate, br";

    private final String acceptLanguage = "zh-CN,zh;q=0.9,zh-TW;q=0.8";

    private final String referer = "https://www.zhihu.com/knowledge-plan/hot-question/hot/0/day";

    private final String secChUa = "\"Google Chrome\";v=\"107\", \"Chromium\";v=\"107\", \"Not=A?Brand\";v=\"24\"";

    private final String secChUaMobile = "?0";

    private final String secChUaPlatform ="\"macOS\"";

    private final String secFetchDest = "empty";

    private final String secFetchMode = "cors";

    private final String secFetchSite = "same-origin";

    public ZhiHuDTO getZhiHuResp() throws URISyntaxException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        URI uri = new URIBuilder(source).setParameter("domain", "0").setParameter("period","day").build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.addHeader(HttpHeaders.COOKIE, ZhiHuCookieDTO.cookie);
        httpGet.addHeader(HttpHeaders.USER_AGENT,userAgent);
        httpGet.addHeader(HttpHeaders.ACCEPT,accept);
//        httpGet.addHeader(HttpHeaders.ACCEPT_ENCODING,acceptEncoding);
        httpGet.addHeader(HttpHeaders.ACCEPT_LANGUAGE,acceptLanguage);
        httpGet.addHeader(HttpHeaders.REFERER,referer);
        httpGet.addHeader("sec-ch-ua",secChUa);
        httpGet.addHeader("sec-ch-ua-mobile",secChUaMobile);
        httpGet.addHeader("sec-ch-ua-platform",secChUaPlatform);
        httpGet.addHeader("sec-fetch-dest",secFetchDest);
        httpGet.addHeader("sec-fetch-mode",secFetchMode);
        httpGet.addHeader("sec-fetch-site",secFetchSite);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return objectMapper.readValue(content,ZhiHuDTO.class);
            }
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }

    public void dingTalkPush(ZhiHuDTO zhiHuDTO) {
        StringBuilder text = new StringBuilder();
        text.append("## [FishMan] 知乎热榜  \\n  ");
        for (ZhiHuDTO.JsonData data :
                zhiHuDTO.getData()) {
            ZhiHuDTO.Question question = data.getQuestion();
            text.append("> ##### ").append(question.getTitle()).append("  \\n  ");
            text.append("> ").append(question.getUrl()).append("  \\n  ");
        }
        String requestBody =
                "{\n" +
                "     \"msgtype\": \"markdown\",\n" +
                "     \"markdown\": {\n" +
                "         \"title\":\"[FishMan] 知乎热榜\",\n" +
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

    public ZhiHuDTO getZhiHuRespBak() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
//        CloseableHttpClient httpClient = HttpClients.custom().build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, ZhiHuCookieDTO.cookie);
        headers.add(HttpHeaders.USER_AGENT,userAgent);
        headers.add(HttpHeaders.ACCEPT,accept);
//        headers.add(HttpHeaders.ACCEPT_ENCODING,acceptEncoding);
        headers.add(HttpHeaders.ACCEPT_LANGUAGE,acceptLanguage);
        headers.add(HttpHeaders.REFERER,referer);
        headers.add("sec-ch-ua",secChUa);
        headers.add("sec-ch-ua-mobile",secChUaMobile);
        headers.add("sec-ch-ua-platform",secChUaPlatform);
        headers.add("sec-fetch-dest",secFetchDest);
        headers.add("sec-fetch-mode",secFetchMode);
        headers.add("sec-fetch-site",secFetchSite);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ZhiHuDTO> responseEntity;
        try {
            responseEntity = restTemplate
                    .exchange(
                            source,
                            HttpMethod.GET,
                            httpEntity,
                            ZhiHuDTO.class
                    );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw e;
        }
    }

    public void dingTalkPushTopHub() {
        String requestBody =
                "{\n" +
                "    \"keyword\": \"知乎\",\n" +
                "    \"msgtype\": \"feedCard\",\n" +
                "    \"feedCard\": {\n" +
                "        \"links\": [";
        requestBody +=
                "{\n" +
                "                \"title\": \"" + "[知乎]  第三方知乎 \", \n" +
                "                \"messageURL\": \"https://tophub.today/n/mproPpoq6O\", \n" +
                "                \"picURL\": \"https://pic2.zhimg.com/80/v2-f6b1f64a098b891b4ea1e3104b5b71f6_720w.png\"\n" +
                "            },";
        requestBody +=
                "]\n" +
                "    }\n" +
                "}";
        CustomDingTalkPusher customDingTalkPusher = new CustomDingTalkPusher();
        try {
            customDingTalkPusher.pushText(requestBody);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            log.error("钉钉发送失败",e);
        }
    }


    public Map<String, String> getFromJsoup() throws IOException {
        Document document = Jsoup
                .connect(source)
                .userAgent(userAgent)
                .header(HttpHeaders.ACCEPT,accept)
                .header(HttpHeaders.ACCEPT_ENCODING,acceptEncoding)
                .header(HttpHeaders.ACCEPT_LANGUAGE,acceptLanguage)
                .header("Sec-Fetch-Dest","document")
                .header("Sec-Fetch-Mode","navigate")
                .header("Sec-Fetch-Site","cross-site")
                .get();
//        Elements elements = document.select("tr");
        Elements elements = document.select("cc-dc").get(0).select("al");
        Map<String, String> resultMap = new HashMap<>();
        for (Element element :
                elements) {
            try {
                Element tInfo = element.select(".t-info").select("a").get(0);
                String tag  = element.select(".t-label").select("a").get(0).text();
                resultMap.put(tag + tInfo.text(),tInfo.attr("abs:href"));
            } catch (Exception e) {
                log.warn("数据读取异常",e);
            }
        }
        return resultMap;
    }

    public String get() throws URISyntaxException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        URI uri = new URIBuilder(source).setParameter("domain", "0").setParameter("period","day").build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
        return null;
    }

    /**
     * Gzip解压缩
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] unGZip(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            byte[] buf = new byte[4096];
            int len = -1;
            while ((len = gzipInputStream.read(buf, 0, buf.length)) != -1) {
                byteArrayOutputStream.write(buf, 0, len);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            byteArrayOutputStream.close();
        }
    }



    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return requestFactory;
    }

}
