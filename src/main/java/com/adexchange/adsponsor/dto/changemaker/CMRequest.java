package com.adexchange.adsponsor.dto.changemaker;

import lombok.Data;

@Data
public class CMRequest {
    private Integer n;
    private String apv;
    private String bid;
    private String aid;
    private Integer adt;
    private Integer adsw;
    private Integer adsh;
    private Integer timeout;
    private TerminalInfo ti;
    private Integer cootype;
    private Float bidprice;

    public static class TerminalInfo {
        private String bn;
        private String hm;
        private String ht;
        private Integer os;
        private String ov;
        private Integer sw;
        private Integer sh;
        private String ch;
        private String ei;
        private String si;
        private Integer nt;
        private String mac;
        private String andid;
        private String idfa;
        private String oid;
        private String uid;
        private String ip;
        private String ua;
        private Integer dpi;
        private String smd;
        private String lon;
        private String lat;
        private String pkg;
        private String apnm;
        private Integer dplink;
    }
}
