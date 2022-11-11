package com.github.skysilently.dingtalk.collector.notificationgetter.qihoo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class QiHooDailyUpdaterTest {
    @Autowired
    private QiHooUpdater qiHooUpdater;
    @Autowired
    private QiHooDailyUpdater qiHooDailyUpdater;

    @Test
    public void test() {
//        qiHooDailyUpdater.pushDaily();
//        qiHooUpdater.checkUpdate();
//        qiHooUpdater.init();
    }

    @Test
    public void testReplace() {
        String str = "## \\[ZSEC\\]360每日简报  \\n  ### 安全事件 Security Incident  \\n  > ##### 研究人员发现NPM供应链攻击“完全自动化”  \\n  > https://t.co/zdoqCWbpti  \\n  > ##### 被黑的WordPress网站被用作对乌克兰发起DDoS攻击的工具  \\n  > https://t.co/hJPbjJp69b  \\n  > ##### 乌克兰拆除俄罗斯5个虚假信息机器人农场  \\n  > https://t.co/tkCqbWcWAi  \\n  > ##### 乌克兰主要互联网服务提供商Ukrtelecom的流量中断  \\n  > https://t.co/2kjFGGjHa0  \\n  > ##### 研究人员破解本田汽车的遥控无钥匙系统（CVE-2022-27254）  \\n  > https://t.co/H11OGdweMB  \\n  > ##### 美国提出医疗网络安全法案  \\n  > https://t.co/pA6se9dOd5  \\n  > ##### 美国公布下一年网络预算请求  \\n  > https://t.co/FBONPzk54h  \\n  > ##### 俄罗斯因设备短缺而面临互联网中断  \\n  > https://t.co/dc6jGBaOaz  \\n  > ##### 德国当局扣押间谍软件公司FinFisher的账户  \\n  > https://t.co/Wd4OYGPhum  \\n  > ##### \"紫狐\"黑客使用了FatalRAT的新变种  \\n  > https://t.co/53bGQGjfOY  \\n  > ##### Muhstik僵尸网络以Redis服务器为目标  \\n  > https://t.co/3FAeiSrMly  \\n  ";
        str.replaceAll("\"","");
        System.out.println(str);
    }
}