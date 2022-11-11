package com.github.skysilently.dingtalk.collector.plugingetter.tomcat;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class Tomcat8ItemUpdater extends TomcatItemUpdater{
    private String plugin = "Tomcat8";

    private String url = "http://tomcat.apache.org/security-8.html";
}
