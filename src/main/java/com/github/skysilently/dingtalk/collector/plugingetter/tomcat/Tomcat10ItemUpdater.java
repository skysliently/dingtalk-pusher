package com.github.skysilently.dingtalk.collector.plugingetter.tomcat;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class Tomcat10ItemUpdater extends TomcatItemUpdater{
    private String plugin = "Tomcat10";

    private String url = "http://tomcat.apache.org/security-10.html";
}
