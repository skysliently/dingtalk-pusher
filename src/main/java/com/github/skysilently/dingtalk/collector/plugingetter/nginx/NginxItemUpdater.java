package com.github.skysilently.dingtalk.collector.plugingetter.nginx;

import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class NginxItemUpdater extends JsoupUpdater {

    private String plugin = "Nginx";

    private String url = "http://nginx.org/en/security_advisories.html";

//    @Autowired
//    SecItemListService secItemListService;


    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) throws IOException {
        ArrayList<SecItemListModel> secItemListModelArrayList = new ArrayList<>();
        this.getFromUrl();
        log.info("Plugin : {};\n" +
                "Url :{}", this.getPlugin(), this.getUrl());
        Elements items = this.getDocument().select("div ul p");
        int intNum = 0;
        if (itemSize.equals("three")) {
            intNum = 3;
        }else if (itemSize.equals("all")) {
            intNum = items.size();
        }
        for (int i = 0; i < intNum; i++) {
            SecItemListModel secItemListModel =  new SecItemListModel();
            secItemListModel.setPlugin(this.getPlugin());
            String[] informations = items.get(i).html().split("<br>");
            secItemListModel.setItem(informations[0]);
            String secInformLink = items.get(i).select("a[href^=http://mailman.nginx.org/pipermail/nginx-announce]").attr("href");
            if (!secInformLink.equals("")) {
                secItemListModel.setSecInformLink(secInformLink);
                Document secInformLinkDoc = Jsoup.connect(secInformLink).get();
                secItemListModel.setPublishTimeOrigin(secInformLinkDoc.select("body > i").text());
                secItemListModel.setDescription(secInformLinkDoc.select("body > pre").text());
            }
            if (informations[1].split(": ")[1].matches("(.*)major(.*)")) {
                secItemListModel.setRisk("major");
            }else secItemListModel.setRisk(informations[1].split(": ")[1]);
            StringBuilder cveIds = new StringBuilder();
            for (Element cveId :
                    items.get(i).select("a[href^=http://cve.mitre.org/cgi-bin/],a[href^=http://www.kb.cert.org/vuls/id/],a[href^=http://www.coresecurity.com/content/]")) {
                cveIds.append(cveId.text()).append("\n");
            }
            secItemListModel.setCveId(cveIds.toString());
            secItemListModelArrayList.add(secItemListModel);
        }
        Collections.reverse(secItemListModelArrayList);
        return secItemListModelArrayList;
    }
}