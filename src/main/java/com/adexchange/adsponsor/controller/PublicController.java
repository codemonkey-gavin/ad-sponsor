package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.dto.womusic.review.AdvertiserQualification;
import com.adexchange.adsponsor.dto.womusic.review.CreativeMaterial;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.service.AdDispatcherService;
import com.adexchange.adsponsor.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PublicController {
    private final static String ERROR_MESSAGE = "Task error";
    private final static String TIME_MESSAGE = "Task timeout";
    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private AdDispatcherService adDispatcherService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/bid/{token}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest,
                          HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        //参数验证
        if (null == bidRequest.getImp()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        if (null == bidRequest.getDevice()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        BidRequest.BidRequestRecord record = new BidRequest.BidRequestRecord();
        if (StringUtils.isEmpty(bidRequest.getDevice().getOs())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        String os = bidRequest.getDevice().getOs();
        switch (os.toLowerCase()) {
            case "ios":
                record.setOs(1);
                break;
            case "android":
                record.setOs(2);
                break;
            case "wp":
                record.setOs(3);
                break;
            default:
                record.setOs(0);
                break;
        }
        record.setAdmode(bidRequest.getImp()[0].getInstl());
        bidRequest.setRecord(record);

        String advertiserId = token;
        if (token.equals("582241fcec6842beae98d27f9a51d8da")) {
            advertiserId = "10002";
        }
        try {
            String bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);
            if (StringUtils.isEmpty(bidResponse)) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            return bidResponse;
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "";
        }
    }

    @RequestMapping(value = "/creative_upload/{token}", method = RequestMethod.POST, produces = "application/json;" +
            "charset=UTF-8")
    public String creativeUpload(@PathVariable("token") String token, @RequestBody CreativeMaterial creativeMaterial, HttpServletResponse response) {
        if (!"c6819b097bc647dab260084ae0586265".equals(token)) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "";
        }
        log.trace(JSON.toJSONString(creativeMaterial));
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
//        if (null != creativeMaterial && creativeMaterial.getCreative().length > 0) {
//            json.put("code", 0);
//            for (CreativeMaterial.Creative creative : creativeMaterial.getCreative()) {
//                if (StringUtils.isEmpty(creative.getCreativeid())) {
//                    continue;
//                }
//                JSONObject jo = new JSONObject();
//                jo.put("creativeid", creative.getCreativeid());
//                array.add(jo);
//            }
//            json.put("data", array);
//            json.put("message", "");
//        } else {
//            json.put("code", 1);
//            json.put("data", array);
//            json.put("message", "请求参数错误");
//        }
        return JSON.toJSONString(json);
    }

    @RequestMapping(value = "/advertiser/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String advertiserReview(@RequestBody AdvertiserQualification.AQReviewRequest reviewRequest) {
        log.info("上传广告主审核资料，{}", JSON.toJSONString(reviewRequest));

        AdvertiserQualification.AQReviewResponse reviewResponse = new AdvertiserQualification.AQReviewResponse();
        if (!"10005".equals(reviewRequest.getDsp_id()) || !"c6819b097bc647dab260084ae0586265".equals(reviewRequest.getToken())) {
            reviewResponse.setStatus(422);
            reviewResponse.setMessage("参数错误");
            reviewResponse.setAdvertisers(new AdvertiserQualification.AQReviewResponseDetail[0]);
            return JSON.toJSONString(reviewResponse);
        }

        if (null != reviewRequest.getAdvertisers()) {
            reviewResponse.setStatus(200);
            reviewResponse.setMessage("");
            List<AdvertiserQualification.AQReviewResponseDetail> list = new ArrayList<>();
            for (AdvertiserQualification.AQReviewRequestDetail detail : reviewRequest.getAdvertisers()) {
                AdvertiserQualification.AQReviewResponseDetail model = new AdvertiserQualification.AQReviewResponseDetail();
                String id = StringUtil.getUUID();
                //记录缓存
                String key = new StringBuffer().append("review_advertiser_").append(id).toString();
                redisTemplate.opsForValue().set(key, JSON.toJSONString(detail));

                model.setIndex(detail.getIndex());
                model.setId(id);
                model.setStatus(200);
                model.setMessage("");
                list.add(model);
            }

            AdvertiserQualification.AQReviewResponseDetail[] array =
                    new AdvertiserQualification.AQReviewResponseDetail[list.size()];
            reviewResponse.setAdvertisers(list.toArray(array));
            log.info("返回响应广告主审核，{}", JSON.toJSONString(reviewResponse));
            return JSON.toJSONString(reviewResponse);
        } else {
            reviewResponse.setStatus(422);
            reviewResponse.setMessage("参数错误");
            reviewResponse.setAdvertisers(new AdvertiserQualification.AQReviewResponseDetail[0]);
            return JSON.toJSONString(reviewResponse);
        }
    }

    @RequestMapping(value = "/advertiser/fetch", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String advertiserReviewFetch(@RequestBody AdvertiserQualification.AQFetchRequest fetchRequest) {
        log.info("查询广告主审核资料，{}", JSON.toJSONString(fetchRequest));

        AdvertiserQualification.AQFetchResponse fetchResponse = new AdvertiserQualification.AQFetchResponse();
        if (!"10005".equals(fetchRequest.getDsp_id()) || !"c6819b097bc647dab260084ae0586265".equals(fetchRequest.getToken())) {
            fetchResponse.setStatus(422);
            fetchResponse.setMessage("参数错误");
            fetchResponse.setAdvertisers(new AdvertiserQualification.AQFetchResponseDetail[0]);
            return JSON.toJSONString(fetchResponse);
        }

        if (null != fetchRequest.getAdvertisers()) {
            fetchResponse.setStatus(200);
            fetchResponse.setMessage("");
            List<AdvertiserQualification.AQFetchResponseDetail> list = new ArrayList<>();
            for (String id : fetchRequest.getAdvertisers()) {
                //查询缓存
                String key = new StringBuffer().append("review_advertiser_").append(id).toString();
                String advertiserInfo = redisTemplate.opsForValue().get(key);
                AdvertiserQualification.AQFetchResponseDetail model = new AdvertiserQualification.AQFetchResponseDetail();
                model.setId(id);
                if (StringUtils.isEmpty(advertiserInfo)) {
                    model.setStatus(404);
                    model.setCheck_message("广告主不存在");
                } else {
                    model.setStatus(200);
                    model.setCheck_status(2);
                    model.setCheck_message("");
                }
                list.add(model);
            }
            AdvertiserQualification.AQFetchResponseDetail[] array =
                    new AdvertiserQualification.AQFetchResponseDetail[list.size()];
            fetchResponse.setAdvertisers(list.toArray(array));
            log.info("返回响应广告主查询，{}", JSON.toJSONString(fetchResponse));
            return JSON.toJSONString(fetchResponse);
        } else {
            fetchResponse.setStatus(422);
            fetchResponse.setMessage("参数错误");
            fetchResponse.setAdvertisers(new AdvertiserQualification.AQFetchResponseDetail[0]);
            return JSON.toJSONString(fetchResponse);
        }
    }

    @RequestMapping(value = "/creative/upload", method = RequestMethod.POST, produces = "application/json;" +
            "charset=UTF-8")
    public String creativeReview(@RequestBody CreativeMaterial.CMReviewRequest reviewRequest) {
        log.info("上传素材审核资料，{}", JSON.toJSONString(reviewRequest));
        CreativeMaterial.CMReviewResponse reviewResponse = new CreativeMaterial.CMReviewResponse();
        if (!"10005".equals(reviewRequest.getDsp_id()) || !"c6819b097bc647dab260084ae0586265".equals(reviewRequest.getToken())) {
            reviewResponse.setStatus(422);
            reviewResponse.setMessage("参数错误");
            reviewResponse.setCreatives(new CreativeMaterial.CMReviewResponseDetail[0]);
            return JSON.toJSONString(reviewResponse);
        }
        if (null != reviewRequest.getCreatives()) {
            reviewResponse.setStatus(200);
            reviewResponse.setMessage("");
            List<CreativeMaterial.CMReviewResponseDetail> list = new ArrayList<>();
            for (CreativeMaterial.CMReviewRequestDetail detail : reviewRequest.getCreatives()) {
                CreativeMaterial.CMReviewResponseDetail model = new CreativeMaterial.CMReviewResponseDetail();

                String id = StringUtil.getUUID();
                //记录缓存
                String key = new StringBuffer().append("review_creative_").append(id).toString();
                redisTemplate.opsForValue().set(key, JSON.toJSONString(detail));
                model.setIndex(detail.getIndex());
                model.setId(id);
                model.setStatus(200);
                model.setMessage("");
                list.add(model);
            }

            CreativeMaterial.CMReviewResponseDetail[] array = new CreativeMaterial.CMReviewResponseDetail[list.size()];
            reviewResponse.setCreatives(list.toArray(array));
            log.info("返回响应素材审核，{}", JSON.toJSONString(reviewResponse));
            return JSON.toJSONString(reviewResponse);
        } else {
            reviewResponse.setStatus(422);
            reviewResponse.setMessage("参数错误");
            reviewResponse.setCreatives(new CreativeMaterial.CMReviewResponseDetail[0]);
            return JSON.toJSONString(reviewResponse);
        }
    }

    @RequestMapping(value = "/creative/fetch", method = RequestMethod.POST, produces = "application/json;" +
            "charset=UTF-8")
    public String creativeReviewFetch(@RequestBody CreativeMaterial.CMFetchRequest fetchRequest) {
        log.info("查询素材审核资料，{}", JSON.toJSONString(fetchRequest));
        CreativeMaterial.CMFetchResponse fetchResponse = new CreativeMaterial.CMFetchResponse();
        if (!"10005".equals(fetchRequest.getDsp_id()) || !"c6819b097bc647dab260084ae0586265".equals(fetchRequest.getToken())) {
            fetchResponse.setStatus(422);
            fetchResponse.setMessage("参数错误");
            fetchResponse.setCreatives(new CreativeMaterial.CMFetchResponseDetail[0]);
            return JSON.toJSONString(fetchResponse);
        }
        if (null != fetchRequest.getCreatives()) {
            fetchResponse.setStatus(200);
            fetchResponse.setMessage("");
            List<CreativeMaterial.CMFetchResponseDetail> list = new ArrayList<>();
            for (String id : fetchRequest.getCreatives()) {
                //查询缓存
                String key = new StringBuffer().append("review_creative_").append(id).toString();
                String creativeInfo = redisTemplate.opsForValue().get(key);
                CreativeMaterial.CMFetchResponseDetail model = new CreativeMaterial.CMFetchResponseDetail();
                model.setId(id);
                if (StringUtils.isEmpty(creativeInfo)) {
                    model.setStatus(404);
                    model.setCheck_message("素材不存在");
                } else {
                    model.setStatus(200);
                    model.setCheck_status(2);
                    model.setCheck_message("");
                }
                list.add(model);
            }
            CreativeMaterial.CMFetchResponseDetail[] array =
                    new CreativeMaterial.CMFetchResponseDetail[list.size()];
            fetchResponse.setCreatives(list.toArray(array));
            log.info("返回响应素材查询，{}", JSON.toJSONString(fetchResponse));
            return JSON.toJSONString(fetchResponse);
        } else {
            fetchResponse.setStatus(422);
            fetchResponse.setMessage("参数错误");
            fetchResponse.setCreatives(new CreativeMaterial.CMFetchResponseDetail[0]);
            return JSON.toJSONString(fetchResponse);
        }
    }
}
