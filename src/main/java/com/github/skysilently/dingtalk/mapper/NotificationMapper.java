package com.github.skysilently.dingtalk.mapper;

import com.github.skysilently.dingtalk.module.dao.NotificationDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NotificationMapper {

    List<NotificationDO> getAllNotifications();

    List<NotificationDO> getNotificationsBySourceAndTypeAndLimitNum(@Param("source")String source, @Param("type")String type, @Param("limitNum")int limitNum);

//    @Select("SELECT * " +
//            "FROM " +
//            "polu_sec_notification_info " +
//            "WHERE " +
//            "date_sub(CURDATE(),INTERVAL 2 DAY) <= DATE(publish_date) " +
//            "AND type='warning' " +
//            "AND source <> source " +
//            "ORDER BY id DESC ")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "source", column = "source"),
//            @Result(property = "infoTitle", column = "info_title"),
//            @Result(property = "type", column = "type"),
//            @Result(property = "link", column = "link"),
//            @Result(property = "description", column = "description"),
//            @Result(property = "publishDate", column = "publish_date"),
//            @Result(property = "createTime", column = "create_time"),
//            @Result(property = "updateTime", column = "update_time"),
//            @Result(property = "detailImg", column = "detail_img"),
//    })
    List<NotificationDO> getWarningNotificationsWithoutSelfLimit2Days(@Param("source")String source);

//    @Insert("INSERT INTO " +
//            "polu_sec_notification_info(source,info_title,type,link,description,publish_date,detail_img) " +
//            "VALUES " +
//            "(#{source},#{infoTitle},#{type},#{link},#{description},#{publishDate},#{detailImg})")
//    @Options(useGeneratedKeys = true,keyProperty = "id")
    void addNotification(NotificationDO notificationDO);
}
