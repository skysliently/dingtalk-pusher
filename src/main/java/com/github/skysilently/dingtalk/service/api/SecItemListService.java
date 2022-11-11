package com.github.skysilently.dingtalk.service.api;

import com.github.skysilently.dingtalk.module.dto.module.SecItemListModel;

import java.util.List;

public interface SecItemListService {

    SecItemListModel getLastSecItemList(String plugin);

    List<SecItemListModel> getLast3SecItemList(String plugin);

    List<SecItemListModel> getAllSecItemList(String plugin);

    int addSecItem(SecItemListModel secItemListModel);

    int addSecItems(List<SecItemListModel> secItemListModelList);

}
