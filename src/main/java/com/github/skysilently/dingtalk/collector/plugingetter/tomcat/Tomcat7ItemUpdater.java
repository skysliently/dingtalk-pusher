package com.github.skysilently.dingtalk.collector.plugingetter.tomcat;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class Tomcat7ItemUpdater extends TomcatItemUpdater{
    private String plugin = "Tomcat7";

    private String url = "http://tomcat.apache.org/security-7.html";
}
