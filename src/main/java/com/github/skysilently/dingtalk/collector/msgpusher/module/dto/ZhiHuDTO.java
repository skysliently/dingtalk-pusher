package com.github.skysilently.dingtalk.collector.msgpusher.module.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author keyun
 * @create 2022-11-11 11:07
 **/
@Data
public class ZhiHuDTO implements Serializable {

    private static final long serialVersionUID = -3523278689279375010L;

    private List<JsonData> data;
    private Paging paging;

    @Data
    public static class JsonData implements Serializable {
        private static final long serialVersionUID = -7793541039820196193L;
        private Question question;
        private Reaction reaction;
    }

    @Data
    public static class Question implements Serializable {

        private static final long serialVersionUID = 1155278910594608390L;

        private String url;

        private Long created;

        private Long updated_time;

        private String title;

        private String highlight_title;

        private String type;

        private String id;

        private Long token;

        private Boolean is_recent_hot;

        private Boolean have_answer;

        private String question_answer_url;

        private List<Topic> topics;
        private String label;

        private Creator creator;

    }

    @Data
    public static class Topic implements Serializable {
        private static final long serialVersionUID = 9010620738669035073L;
        private Long url_token;
        private String name;
    }

    @Data
    private static class Creator implements Serializable {
        private static final long serialVersionUID = -6656760501110803841L;
        private String url_token;
        private String name;
    }

    @Data
    private static class Reaction implements Serializable{
        private static final long serialVersionUID = -8725499130135173063L;
        private Long new_pv;
        private Long new_pv_7_days;
        private Long new_follow_num;
        private Long new_follow_num_7_days;
        private Long new_answer_num;
        private Long new_answer_num_7_days;
        private Long new_upvote_num;
        private Long new_upvote_num_7_days;
        private Long pv;
        private Long follow_num;
        private Long answer_num;
        private Long upvote_num;
        private String pv_incr_rate;
        private String head_percent;
        private Long new_pv_yesterday;
        private Long new_pv_t_yesterday;
        private Long score;
        private Long score_level;
        private String text;
    }

    @Data
    private class Paging implements Serializable{
        private static final long serialVersionUID = 1544143560689905751L;
        private Boolean is_end;
        private Boolean is_start;
        private String next;
        private String previous;
        private Integer totals;
    }
}
