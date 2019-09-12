package com.adexchange.adsponsor.dto.changemaker;

import lombok.Data;

@Data
public class CMResponse {
    private Integer rcd;
    private String msg;
    private Ad[] ad;

    public static class Ad {
        private Integer at;
        private String adl;
        private Integer width;
        private Integer height;
        private String clk;
        private String ati;
        private String atx;
        private String pack;
        private String appname;
        private Integer deeplinktype;
        private String deeplink;
        private String[] std;
        private String[] dcp;
        private String[] ital;
        private String[] ec;
        private String[] es;
        private String[] durls;
        private Integer act;
        private String wurl;
    }
}
