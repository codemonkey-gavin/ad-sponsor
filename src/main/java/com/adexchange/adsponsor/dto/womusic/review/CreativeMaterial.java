package com.adexchange.adsponsor.dto.womusic.review;

import lombok.Data;

import java.util.Date;

public class CreativeMaterial {
    @Data
    public static class CMReviewRequest {
        // SSP 分配的 dspId
        private String dsp_id;
        // SSP 分配的 token
        private String token;
        //素材数组，一次请求最多支持上传 100 组Array 素材
        private CMReviewRequestDetail[] creatives;
    }

    @Data
    public static class CMReviewRequestDetail {
        //序号，要求在一次请求内多组素材的序号唯一，响应中每个素材对象会携带该序号，用标识素材。
        private Integer index;
        //审核通过的广告主 id
        private String advertiser_id;
        //产品名称,最大 255 个字符
        private String product_name;
        //内容所属行业 id
        private Integer industry;
        //素材地址数组，可以是多个素材
        private String[] urls;
        //素材宽
        private Integer width;
        //素材高
        private Integer height;
        //素材文件类型，枚举值 1-mp4，2- swf，3- flv， 4-gif，5 – png，6 - jpg(jpeg)，7-其他类型。要求以数字传值，多种类型用英文逗号隔开。
        private String mime;
        //素材类型 1 - 图片 2 - 视频
        private Integer creative_type;
        //标题,最大 255 个字符
        private String title;
        //描述,最大 255 个字符
        private String description;
        //落地页 url
        private String landing;
        //落地页类型
        //1 - 跳转类
        //2 - 下载类
        //3 - 纯展示
        private String landing_type;
        //Deeplink
        private String deeplink;
        //视频素材的时长,单位 ms
        private Integer play_time;
        //广告源标记，根据广告法要求用来标明广告来源,最大 255 个字符
        private String source;
        //品牌名称，最大 255 个字符
        private String brand;
        //下载类广告的应用包名,最大 255个字符
        private String app_package;
        //下载类广告的应用名称,最大 255个字符
        private String app_name;
        //下载类广告的应用版本号,最大255 个字符
        private String app_version;
        //下载类广告的应用图标地址,最 大 255 个字符
        private String app_icon;
        //格式'yyyy-MM-dd'，素材有效的开始时间
        private Date enable_start;
        //格式'yyyy-MM-dd'，素材有效的结束时间，要求大于开始时间且大于当前日期
        private Date enable_end;
    }

    @Data
    public static class CMReviewResponse {
        //响应状态码
        //200 - 成功
        //401 - 身份认证失败
        //422 - 失败，参数错误
        //500 - 失败，系统异常
        private Integer status;
        //status != 200 时，该字段填充错误信息
        private String message;
        //素材数组
        private CMReviewResponseDetail[] creatives;
    }

    @Data
    public static class CMReviewResponseDetail {
        //序号。与请求参数中序号对应，用以对应请求和响应中的素材。
        private Integer index;
        //素材送审结果
        //200 - 成功
        //422 - 失败，参数错误
        private Integer status;
        //status != 200 时，填充送审失败原因
        private String message;
        //素材 id，用于后续审核状态查询
        private String id;
    }

    @Data
    public static class CMFetchRequest {
        // SSP 分配的 dspId
        private String dsp_id;
        // SSP 分配的 token
        private String token;
        //待查询的素材 id 数组,一次请求最多支持查询100组素材
        private String[] creatives;
    }

    @Data
    public static class CMFetchResponse {
        //响应状态码
        //200 - 成功
        //401 - 身份认证失败
        //422 - 失败，参数错误
        //500 - 失败，系统异常
        private Integer status;
        //status != 200 时，该字段填充错误信息
        private String message;
        //素材审核结果信息数组
        private CMFetchResponseDetail[] creatives;
    }

    @Data
    public static class CMFetchResponseDetail {
        //素材 id
        private String id;
        //查询结果
        //200 - 成功
        //404 - 失败，素材不存在
        private Integer status;
        //审核状态
        //1 - 审核中
        //2 - 审核通过
        //3 - 审核不通过
        private Integer check_status;
        //审核失败说明
        private String check_message;
    }
}
