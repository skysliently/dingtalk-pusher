package com.github.skysilently.dingtalk.collector.dingtalkcustom;

public class DingTalkUtil {

    public static String transformQuotationMark(String quotation) {
        quotation = quotation.replace("\"","&quot;");
        quotation = quotation.replace("'","&apos;");
        return quotation;
    }

    public static String transformHTML(String quotation) {
        quotation = quotation.replace("\"","&quot;");
        quotation = quotation.replace("'","&apos;");
        quotation = quotation.replace("&","&amp;");
        return quotation;
    }
}
