package com.adexchange.adsponsor.dto.inmobi;

import lombok.Data;

import java.util.Dictionary;

@Data
public class IMResponse {
    private String requestId;
    private IMAd[] ads;
    @Data
    public static class IMAd {
        private PubContent pubContent;
        private String landingPage;
        private Dictionary<String, IMUrl> eventTracking;
        private Boolean isApp;
        private Boolean openExternal;
    }
    @Data
    public static class PubContent {
        private String title;
        private String description;
        private String landingURL;
        private String cta;
        private IMCreative icon;
        private IMCreative screenshots;
    }
    @Data
    public static class IMCreative {
        private Integer width;
        private Integer height;
        private String url;
        private Float aspectRatio;
    }
    @Data
    public static class IMUrl {
        public String[] urls;
    }
}
