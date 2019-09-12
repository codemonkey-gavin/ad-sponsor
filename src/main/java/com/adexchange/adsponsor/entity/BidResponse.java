package com.adexchange.adsponsor.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BidResponse {
    @NonNull
    private String id;
    private SeatBid[] seatbid;
    private String bidid;
    private String cur = "USD";
    private String customdata;
    @NonNull
    private Integer nbr;
    private Object ext;

    @Data
    public static class SeatBid {
        private Bid[] bid;
        private String seat;
        private Integer group = 0;
        private Object ext;
    }

    @Data
    public static class Bid {
        private String id;
        private String impid;
        private Float price;
        private String nurl;
        private String burl;
        private String lurl;
        private String adm;
        private String adid;
        private String[] adomain;
        private String bundle;
        private String iurl;
        private String cid;
        private String crid;
        private String tactic;
        private String[] cat;
        private Integer[] attr;
        private Integer api;
        private Integer protocol;
        private Integer qagmediarating;
        private String language;
        private String dealid;
        private Integer w;
        private Integer h;
        private Integer wratio;
        private Integer hratio;
        private Integer exp;
        private Object ext;
    }

    @Data
    public static class NativeResponse {
        private String ver = "1.2";
        private NativeResponseAsset[] assets;
        private String assetsurl;
        private String dcourl;
        private NativeResponseAssetLink link;
        private String[] imptrackers;
        private String jstracker;
        private NativeResponseEventTracker[] eventtrackers;
        private String privacy;
        private Object ext;
    }

    @Data
    public static class NativeResponseAsset {
        private int id;
        private int required = 0;
        private NativeResponseAssetTitle title;
        private NativeResponseAssetImage img;
        private NativeResponseAssetVideo video;
        private NativeResponseAssetData data;
        private NativeResponseAssetLink link;
        private Object ext;
    }

    @Data
    public static class NativeResponseAssetTitle {
        private String text;
        private Integer len;
        private Object ext;
    }

    @Data
    public static class NativeResponseAssetImage {
        private Integer type;
        private String url;
        private Integer w;
        private Integer h;
        private Object ext;
    }

    @Data
    public static class NativeResponseAssetData {
        private Integer type;
        private Integer len;
        private String value;
        private Object ext;
    }

    @Data
    public static class NativeResponseAssetVideo {
        private String vasttag;
    }

    @Data
    public static class NativeResponseAssetLink {
        private String url;
        private String[] clicktrackers;
        private String fallback;
        private Object ext;
    }

    @Data
    public static class NativeResponseEventTracker {
        private Integer event;
        private Integer method;
        private String url;
        private Object customdata;
        private Object ext;
    }
}
