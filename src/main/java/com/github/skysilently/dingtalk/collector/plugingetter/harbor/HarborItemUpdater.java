package com.github.skysilently.dingtalk.collector.plugingetter.harbor;

import com.github.skysilently.dingtalk.collector.plugingetter.JsoupUpdater;
import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
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

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Service
public class HarborItemUpdater extends JsoupUpdater {

    private String plugin = "Harbor";

    private String url = "https://github.com/goharbor/harbor/security/advisories";

    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) throws IOException {
        ArrayList<SecItemListModel> secItemListModelArrayList = new ArrayList<>();
        this.getFromUrl();
        log.info("Plugin : {};\n" +
                "Url :{}", this.getPlugin(), this.getUrl());
        while (true){
            Elements next = this.getDocument().select("a[class^=next_page]");
            Elements items = this.getDocument().select("div#advisories li");
            int intNum = 0;
            if (itemSize.equals("three")) {
                intNum = 3;
            }else if (itemSize.equals("all")) {
                intNum = items.size();
            }
            for (int i = 0; i < intNum; i++) {
                SecItemListModel secItemListModel =  new SecItemListModel();
                secItemListModel.setPlugin(this.getPlugin());
                secItemListModel.setItem(items.get(i).select("a[href^=/goharbor/harbor/security/advisories/]").text());
                String secInformLink = items.get(i).select("a").attr("abs:href");
                secItemListModel.setSecInformLink(secInformLink);
                Document detailDoc = Jsoup.connect(secInformLink).get();
                secItemListModel.setRisk(detailDoc.select("dl dd span[title^=Severity]").text());
                secItemListModel.setCveId(detailDoc.select("dl dd").last().text());
                secItemListModel.setDescription(detailDoc.select("div[class^=Box-body comment-body markdown-body py-3]").text());
                secItemListModel.setPublishTimeOrigin(detailDoc.select("relative-time").attr("datetime"));
                //                String version = detailDoc.select("dl dd").last().previousElementSibling().text();//版本号不确定性过强
                secItemListModelArrayList.add(secItemListModel);
            }

            if (next.size() == 0) {
                break;
            } else this.setDocument(Jsoup.connect(next.attr("abs:href")).get());
        }
        Collections.reverse(secItemListModelArrayList);
        return secItemListModelArrayList;
    }
}
