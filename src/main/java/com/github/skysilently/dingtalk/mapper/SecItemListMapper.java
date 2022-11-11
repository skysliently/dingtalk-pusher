package com.github.skysilently.dingtalk.mapper;

import com.github.skysilently.dingtalk.module.dao.SecItemListDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SecItemListMapper {

    SecItemListDO getLastSecItemList(@Param("plugin") String plugin);

    /**
     * 返回最新三个条目
     * @return  ArrayList<SecItemListDO>
     */
    List<SecItemListDO> getLast3SecItemList(@Param("plugin") String plugin);

    List<SecItemListDO> getAllSecItemList(@Param("plugin") String plugin);

    /**
     * 插入新的条目
     */
//    @Insert("INSERT INTO " +
//            "polu_opensource_sec_update(plugin,item,sec_inform_link,risk,description,cve_id,publish_time,publish_time_origin,version_range) " +
//            "VALUES " +
//            "(#{plugin},#{item},#{secInformLink},#{risk},#{description},#{cveId},#{publishTime},#{publishTimeOrigin},#{versionRange})")
//    @Options(useGeneratedKeys = true,keyProperty = "id")
    int addSecItem(SecItemListDO secItemListDO);
}
