package com.adexchange.adsponsor.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class BidResponse {
    private String id;
    private SeatBid[] seatbid;
    private String bidid;
    private String cur = "USD";
    private String customdata;
    private Integer nbr = 0;
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
        private BidExt ext;
    }

    @Data
    public static class BidExt {
        //创意素材类型, 1:图片广告，2:图文广告，3:文字链广告，4:html5广告，5:视频广告，6:原生广告
        private Integer admt;
        //广告点击行为类型，0：未确认，1：打开目标链接，2：app下载，3：打开地图，4： 拨打电话，5：发送短信，6：发送邮件，7：播放视频
        private Integer adct;
        //图标
        private String icon;
        //图片
        private String adi;
        //标题
        private String adt;
        //描述
        private String ads;
        //下载类广告的名称，非广告语
        private String dan;
        //广告点击跳转落地页，可以支持重定向
        private String targeturl;
        //包含deeplinkurl的点击跳转地址，无法打开则使用targeturl
        private String deeplinkurl;
        //展示监控链接
        private String[] imptrackers;
        //点击监控链接
        private String[] clicktrackers;
        //开始下载监控链接
        private String[] downloadbegintrackers;
        //下载完成监控链接
        private String[] downloadendtrackers;
        //安装完成监控链接
        private String[] installedtrackers;
        //deeplink媒体打开成功监控链接
        private String[] deeplinktrackers;
    }

    @Data
    static class BidExtImage {
        private Integer w;
        private Integer h;
        private String url;
    }

    @Data
    public static class BidNativeResponse {
        @JSONField(name = "native")
        private NativeResponse nativeResponse;
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
