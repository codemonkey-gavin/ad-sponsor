package com.adexchange.adsponsor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BidRequest {
    private String id;
    private BidRequestImp[] imp;
    private BidRequestSite site;
    private BidRequestApp app;
    private BidRequestDevice device;
    private BidRequestUser user;
    private Integer test = 0;
    private Integer at = 2;
    private Integer tmax;
    private String[] wseat;
    private String[] bseat;
    private Integer allimps = 0;
    private String[] cur;
    private String[] wlang;
    private String[] bcat;
    private String[] badv;
    private String[] bapp;
    private BidRequestSource source;
    private BidRequestRegs regs;
    private Object ext;

    @Data
    public static class BidRequestSource {
        private Integer fd;
        private String tid;
        private String pchain;
        private Object ext;
    }

    @Data
    private static class BidRequestRegs {
        private Integer coppa;
        private Object ext;
    }

    @Data
    public static class BidRequestImp {
        private String id;
        private BidRequestMetric[] metric;
        private BidRequestBanner banner;
        private BidRequestVideo video;
        private BidRequestAudio audio;
        @JSONField(name = "native")
        private BidRequestNative requestNative;
        private BidRequestPmp pmp;
        private String displaymanager;
        private String displaymanagerver;
        private Integer instl = 0;
        private String tagid;
        private Float bidfloor = 0f;
        private String bidfloorcur = "USD";
        private Integer clickbrowser;
        private Integer secure;
        private String[] iframebuster;
        private Integer exp;
        private Object ext;
    }

    @Data
    private static class BidRequestMetric {
        private String type;
        private Float value;
        private String vendor;
        private Object ext;
    }

    @Data
    private static class BidRequestBanner {
        private BidRequestBannerFormat[] format;
        private Integer w;
        private Integer h;
        // DEPRECATED
        //private Integer wmax;
        // DEPRECATED
        //private Integer hmax;
        // DEPRECATED
        //private Integer wmin;
        // DEPRECATED
        //private Integer hmin;
        private Integer[] btype;
        private Integer[] battr;
        private Integer pos;
        private String[] mimes;
        private Integer topframe;
        private Integer[] expdir;
        private Integer[] api;
        private String id;
        private Integer vcm;
        private Object ext;
    }

    @Data
    private static class BidRequestVideo {
        private String[] mimes;
        private Integer minduration;
        private Integer maxduration;
        private Integer[] protocols;
        // DEPRECATED
        //private Integer protocol;
        private Integer w;
        private Integer h;
        private Integer startdelay;
        private Integer placement;
        private Integer linearity;
        private Integer skip;
        private Integer skipmin = 0;
        private Integer skipafter = 0;
        private Integer sequence;
        private Integer[] battr;
        private Integer maxextended;
        private Integer minbitrate;
        private Integer maxbitrate;
        private Integer boxingallowed = 1;
        private Integer[] playbackmethod;
        private Integer playbackend;
        private Integer[] delivery;
        private Integer pos;
        //private Object[] companionad;
        private Integer[] api;
        private Integer[] companiontype;
        private Object ext;
    }

    @Data
    private static class BidRequestAudio {
        private String[] mimes;
        private Integer minduration;
        private Integer maxduration;
        private Integer[] protocols;
        private Integer startdelay;
        private Integer sequence;
        private Integer[] battr;
        private Integer maxextended;
        private Integer minbitrate;
        private Integer maxbitrate;
        private Integer[] delivery;
        //private Object[] companionad;
        private Integer[] api;
        private Integer[] companiontype;
        private Integer maxseq;
        private Integer feed;
        private Integer stitched;
        private Integer nvol;
        private Object ext;
    }

    @Data
    private static class BidRequestNative {
        private String request;
        private String ver;
        private Integer[] api;
        private Integer[] battr;
        private Object ext;
    }

    @Data
    private static class BidRequestBannerFormat {
        private Integer w;
        private Integer h;
        private Integer wratio;
        private Integer hratio;
        private Integer wmin;
        private Object ext;
    }

    @Data
    private static class BidRequestPmp {
        private Integer private_auction = 0;
        private BidRequestPmpDeal[] deals;
        private Object ext;
    }

    @Data
    private static class BidRequestPmpDeal {
        private String id;
        private Float bidfloor = 0f;
        private String bidfloorcur = "USD";
        private Integer at;
        private String[] wseat;
        private String[] wadomain;
        private Object ext;
    }

    @Data
    public static class BidRequestSite {
        private String id;
        private String name;
        private String domain;
        private String[] cat;
        private String[] sectioncat;
        private String[] pagecat;
        private String page;
        private String ref;
        private String search;
        private Integer mobile;
        private Integer privacypolicy;
        private BidRequestPublisher publisher;
        private BidRequestContent content;
        private String keywords;
        private Object ext;
    }

    @Data
    public static class BidRequestApp {
        private String id;
        private String name;
        private String bundle;
        private String domain;
        private String storeurl;
        private String[] cat;
        private String[] sectioncat;
        private String[] pagecat;
        private String ver;
        private Integer privacypolicy;
        private Integer paid;
        private BidRequestPublisher publisher;
        private BidRequestContent content;
        private String keywords;
        private Object ext;
    }

    @Data
    private static class BidRequestPublisher {
        private String id;
        private String name;
        private String[] cat;
        private String domain;
        private Object ext;
    }

    @Data
    private static class BidRequestContent {
        private String id;
        private Integer episode;
        private String title;
        private String series;
        private String season;
        private String artist;
        private String genre;
        private String album;
        private String isrc;
        private BidRequestContentProducer producer;
        private String url;
        private String[] cat;
        private Integer prodq;
        // DEPRECATED
        //private Integer videoquality;
        private Integer context;
        private String contentrating;
        private String userrating;
        private Integer qagmediarating;
        private String keywords;
        private Integer livestream;
        private Integer sourcerelationship;
        private Integer len;
        private String language;
        private Integer embeddable;
        private BidRequestData[] data;
        private Object ext;
    }

    @Data
    private static class BidRequestContentProducer {
        private String id;
        private String name;
        private String[] cat;
        private String domain;
        private Object ext;
    }

    @Data
    public static class BidRequestDevice {
        private String ua;
        private BidRequestGeo geo;
        private Integer dnt;
        private Integer lmt;
        private String ip;
        private String ipv6;
        private Integer devicetype;
        private String make;
        private String model;
        private String os;
        private String osv;
        private String hwv;
        private Integer h;
        private Integer w;
        private Integer ppi;
        private Float pxratio;
        private Integer js;
        private Integer geofetch;
        private String flashver;
        private String language;
        private String carrier;
        private String mccmnc;
        private Integer connectiontype;
        private String ifa;
        private String didsha1;
        private String didmd5;
        private String dpidsha1;
        private String dpidmd5;
        private String macsha1;
        private String macmd5;
        private Object ext;
    }

    @Data
    private static class BidRequestGeo {
        private Float lat;
        private Float lon;
        private Integer type;
        private Integer accuracy;
        private Integer lastfix;
        private Integer ipservice;
        private String country;
        private String region;
        private String regionfips104;
        private String metro;
        private String city;
        private String zip;
        private Integer utcoffset;
        private Object ext;
    }

    @Data
    public static class BidRequestUser {
        private String id;
        private String buyeruid;
        private Integer yob;
        private String gender;
        private String keywords;
        private String customdata;
        private BidRequestGeo geo;
        private BidRequestData[] data;
        private Object ext;
    }

    @Data
    private static class BidRequestData {
        private String id;
        private String name;
        private BidRequestDataSegment[] segment;
        private Object ext;
    }

    @Data
    private static class BidRequestDataSegment {
        private String id;
        private String name;
        private String value;
        private Object ext;
    }

    @Data
    private static class NativeRequest {
        private String ver = "1.2";
        private Integer context;
        private Integer contextsubtype;
        private Integer plcmttype;
        private Integer plcmtcnt = 1;
        private Integer seq = 0;
        private NativeRequestAsset[] assets;
        private int aurlsupport = 0;
        private int durlsupport = 0;
        private NativeRequestEventTracker[] eventtrackers;
        private Integer privacy = 0;
        private Object ext;
    }

    @Data
    private static class NativeRequestAsset {
        private int id;
        private int required = 0;
        private NativeRequestAssetTitle title;
        private NativeRequestAssetImage img;
        private NativeRequestVideo video;
        private NativeRequestData data;
        private Object ext;
    }

    @Data
    private static class NativeRequestAssetTitle {
        private Integer len;
        private Object ext;
    }

    @Data
    private static class NativeRequestAssetImage {
        private Integer type;
        private Integer w;
        private Integer wmin;
        private Integer h;
        private Integer hmin;
        private String[] mimes;
        private Object ext;
    }

    @Data
    private static class NativeRequestVideo {
        private String[] mimes;
        private Integer minduration;
        private Integer maxduration;
        private Integer[] protocols;
        private Object ext;
    }

    @Data
    private static class NativeRequestData {
        private Integer type;
        private Integer len;
        private Object ext;
    }

    @Data
    private static class NativeRequestEventTracker {
        private Integer event;
        private Integer[] methods;
        private Object ext;
    }
}
