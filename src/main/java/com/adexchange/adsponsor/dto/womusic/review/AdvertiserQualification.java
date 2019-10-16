package com.adexchange.adsponsor.dto.womusic.review;

import lombok.Data;

import java.util.Map;

public class AdvertiserQualification {

    @Data
    public static class AQReviewRequest {
        // SSP 分配的 dspId
        private String dsp_id;
        // SSP 分配的 token
        private String token;
        // 送审的广告主信息,支持一次请求送审多组广告主，一次请求最多支持送审 100 个广告主
        private AQReviewRequestDetail[] advertisers;
    }

    @Data
    public static class AQReviewRequestDetail {
        // 序号，要求在一次请求内多组广告主的序号唯一，响应中每个广告主对象会携带该序号，用以标识广告主
        private Integer index;
        // 广告主名称,最大 255 个字符
        private String advertiser_name;
        // 广告主行业 id
        private Integer industry;
        // 公司名称,最大 255 个字符
        private String company_name;
        // 广告主 logo 的 url，最大 255 个字符
        private String logo;
        // 广告主公司官网 url，最大 255 个字符
        private String site;
        // 营业执照 url，最大 255 个字符
        private String business_license;
        // ICP 证 url，最大 255 个字符
        private String icp;
        // 扩展资质（针对一些特殊行业，如String> 医疗类）map 的 key 为资质名称，value 为资质图片地址。
        private Map<String, String> extend_qualifications;
    }

    @Data
    public static class AQReviewResponse {
        //执行结果
        //200 - 成功
        //401 - 身份认证失败
        //422 - 失败，参数错误
        //500 - 失败，系统异常
        private Integer status;
        //status != 200 时，该字段会填充错误信息
        private String message;
        private AQReviewResponseDetail[] advertisers;
    }

    @Data
    public static class AQReviewResponseDetail {
        //序号，与请求参数中序号对应，用以对应请求和响应中的广告主。
        private Integer index;
        //广告主送审结果
        //200 - 成功
        //422 - 参数错误
        private Integer status;
        //status != 200 时，填充送审失败的错误信息
        private String message;
        //广告主 id，status = 200时必填 用于后续审核状态查询和素材送审
        private String id;
    }

    @Data
    public static class AQFetchRequest {
        // SSP 分配的 dspId
        private String dsp_id;
        // SSP 分配的 token
        private String token;
        // 待查询的广告主的 id 数组，一次请求最多支持查询 100 个广告主
        private String[] advertisers;
    }

    @Data
    public static class AQFetchResponse {
        //响应状态码
        //200 - 成功
        //401 - 身份认证失败
        //422 - 失败，参数错误
        //500 - 失败，系统异常
        private Integer status;
        //status != 200 时，该字段填充错误信息
        private String message;
        //广告主审核结果信息数组
        private AQFetchResponseDetail[] advertisers;
    }

    @Data
    public static class AQFetchResponseDetail {
        //广告主 id
        private String id;
        //查询结果
        //200 - 查询成功
        //404 - 查询失败，广告主不存在
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
