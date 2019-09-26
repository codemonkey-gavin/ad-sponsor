package com.adexchange.adsponsor.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreativeMaterial {
    private Creative[] creative;

    @Data
    public static class Creative {
        // 广告主id
        private String advertiserid;
        // 创意id
        private String creativeid;
        // 创意名称
        private String name;
        // 落地页
        private String landingpage;
        // 曝光检测
        private String[] monitor;
        // 创意类型 1 纯图片类（横幅、插屏、开屏等banner广告） 2 图文混排类（信息流）
        private Integer type;
        // 纯图片类
        private CreativeBanner banner;
        // 信息流
        @JsonProperty(value = "native")
        private CreativeNative creativeNative;
    }

    @Data
    public static class CreativeBanner {
        private MaterialImage icon;
        private MaterialImage[] materials;
    }

    @Data
    public static class CreativeNative {
        private String title;
        private String content;
        private MaterialImage icon;
        private MaterialImage[] materials;
    }

    @Data
    public static class MaterialImage {
        // 宽
        private Integer width;
        // 高
        private Integer height;
        // 图片地址
        private String url;
    }
}
