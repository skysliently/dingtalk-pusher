package com.github.skysilently.dingtalk.collector.plugingetter.tomcat;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class Tomcat9ItemUpdater extends TomcatItemUpdater{
    private String plugin = "Tomcat9";

    private String url = "http://tomcat.apache.org/security-9.html";
}
