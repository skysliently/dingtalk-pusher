package com.github.skysilently.dingtalk.collector.plugingetter;

import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;
import com.github.skysilently.dingtalk.service.api.AppDependencyService;
import com.github.skysilently.dingtalk.service.api.SecItemListService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Service
public abstract class JsoupUpdater {

    private String plugin;

    private String html;

    private Document document;

    private String url;

    private String[] items;

    @Autowired
    protected SecItemListService secItemListService;

    @Autowired
    protected AppDependencyService appDependencyService;

    public void getFromUrl() {
        try {
            this.setDocument(Jsoup.connect(this.getUrl()).get());
        } catch (IOException e) {
            log.error("Jsoup connect failed:",e);
        }
    }

    public void init(){
        if (!isEmpty()){
            System.out.println(this.getPlugin() + " dont need init");
            log.info(this.getPlugin() + "dont need init");
            return;
        }
        try {
            ArrayList<SecItemListModel> secItemListModelArrayList = getAllSecItemListModelFromJsoup();
            if (secItemListService.addSecItems(secItemListModelArrayList) == -1) {
                System.out.println("插入数据失败！");
                log.error("Insert data failed ！");
            }
        } catch (IOException e) {
            log.error("get secItemListModel from jsoup failed:",e);
        }
    }

    public void checkUpdate() {
        ArrayList<SecItemListModel> secItemListModelArrayListFromJsoup;
        try {
            secItemListModelArrayListFromJsoup = getLast3SecItemListModelFromJsoup();
        } catch (Exception e) {
            log.error("get secItemListModel from jsoup failed :",e);
            return;
        }
//        Collections.reverse(secItemListModelArrayListFromJsoup);
        SecItemListModel secItemListModel = getLastSecItemListModelFromDB();
        if (secItemListModel == null) {
            log.warn("No plugin named {}", this.getPlugin());
            return;
        }
        while (!secItemListModelArrayListFromJsoup.isEmpty()){
            if (secItemListModelArrayListFromJsoup.get(0).getItem().equals(secItemListModel.getItem())) {
                secItemListModelArrayListFromJsoup.remove(0);
                break;
            }
            else secItemListModelArrayListFromJsoup.remove(0);
        }
        if (!secItemListModelArrayListFromJsoup.isEmpty()) {
            if ( secItemListService.addSecItems(secItemListModelArrayListFromJsoup) != -1) {
                log.info("Data list insert : {}", secItemListModelArrayListFromJsoup);
            }else log.warn("Data list insert failed :{}", secItemListModelArrayListFromJsoup);
        }else log.info("No update exist with plugin {}", this.getPlugin());
    }

    public ArrayList<SecItemListModel> getAllSecItemListModelFromJsoup() throws IOException {
        return getSecItemListModelFromJsoup("all");
    }

    public ArrayList<SecItemListModel> getLast3SecItemListModelFromJsoup() throws IOException {
        return getSecItemListModelFromJsoup("three");
    }

    public ArrayList<SecItemListModel> getSecItemListModelFromJsoup(String itemSize) throws IOException {
//        this.getFromUrl();
        return null;
    }

    public SecItemListModel getLastSecItemListModelFromDB(){
        return secItemListService.getLastSecItemList(this.getPlugin());
    }

    public List<SecItemListModel> getAllSecItemListModelFromDB(){
        return secItemListService.getAllSecItemList(this.getPlugin());
    }

    public boolean isEmpty(){
        return getLastSecItemListModelFromDB() == null;
    }
}
