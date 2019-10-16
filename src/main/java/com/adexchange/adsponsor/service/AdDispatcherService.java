package com.adexchange.adsponsor.service;

import com.adexchange.adsponsor.advertiser.Adview;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Service
public class AdDispatcherService {
    private static Map<String, String> adsMap;
    private static Map<String, Class<?>> clsMap;
    private static Map<String, Method> methodMap;

    public static Map<String, String> initAdsMap() {
        if (adsMap == null) {
            synchronized (AdDispatcherService.class) {
                if (adsMap == null) {
                    Properties pro = new Properties();
                    ClassPathResource classPathResource = new ClassPathResource("ads.properties");
                    try {
                        InputStream inputStream = classPathResource.getInputStream();
                        pro.load(inputStream);
                        inputStream.close();
                        Set<Map.Entry<Object, Object>> entrySet = pro.entrySet();
                        Map<String, String> amap = new HashMap<>();
                        Map<String, Class<?>> cmap = new HashMap<>();
                        Map<String, Method> mmap = new HashMap<>();
                        for (Map.Entry<Object, Object> entry : entrySet) {
                            Class<?> cls = Class.forName(entry.getValue().toString());
                            Method method = cls.getMethod("getAds", BidRequest.class, String.class, Integer.class);
                            cmap.put(entry.getKey().toString(), cls);
                            mmap.put(entry.getKey().toString(), method);
                            amap.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                        adsMap = amap;
                        clsMap = cmap;
                        methodMap = mmap;
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return null;
                    }
                }
            }
        }
        return adsMap;
    }

    public String getAds(BidRequest request, String advertiserId) {
        try {
//            Properties pro = new Properties();
//            ClassPathResource classPathResource = new ClassPathResource("ads.properties");
//            InputStream inputStream = classPathResource.getInputStream();
//            pro.load(inputStream);
//            inputStream.close();
//            String advertiserName = pro.getProperty(advertiserId);
//            if (!StringUtils.isEmpty(advertiserName)) {
//                Class<?> cls = Class.forName(advertiserName);
//                Method method = cls.getMethod("getAds", BidRequest.class, String.class, Integer.class);
//                Object response = method.invoke(cls.newInstance(), request, advertiserId, 200);
//                BidResponse bidResponse = (BidResponse) response;
//
//            }
            Map<String, String> adsMap = initAdsMap();
            if (null != adsMap) {
                Class<?> cls = clsMap.get(advertiserId);
                Method method = methodMap.get(advertiserId);
                Object response = method.invoke(cls.newInstance(), request, advertiserId, 300);
                BidResponse bidResponse = (BidResponse) response;
                // 获取广告成功
                if (null != bidResponse && bidResponse.getNbr() == 200) {
                    //map = BeanMap.create(bidResponse);
                    //响应实例
                    JSONObject map = new JSONObject();
                    map.put("id", bidResponse.getId());
                    map.put("nbr", bidResponse.getNbr());
                    if (!StringUtils.isEmpty(bidResponse.getCur())) {
                        map.put("cur", bidResponse.getCur());
                    }
                    if (!StringUtils.isEmpty(bidResponse.getBidid())) {
                        map.put("bidid", bidResponse.getBidid());
                    }
                    if (!StringUtils.isEmpty(bidResponse.getCustomdata())) {
                        map.put("customdata", bidResponse.getCustomdata());
                    }
                    JSONArray seatbids = new JSONArray();
                    for (BidResponse.SeatBid seatBidItem : bidResponse.getSeatbid()) {
                        JSONObject seatBid = new JSONObject();
                        seatBid.put("group", seatBidItem.getGroup());
                        if (!StringUtils.isEmpty(seatBidItem.getSeat())) {
                            seatBid.put("seat", seatBidItem.getSeat());
                        }
                        JSONArray bids = new JSONArray();
                        for (BidResponse.Bid bidItem : seatBidItem.getBid()) {
                            JSONObject bid = new JSONObject();
                            if (!StringUtils.isEmpty(bidItem.getId())) {
                                bid.put("id", bidItem.getId());
                            }
                            if (!StringUtils.isEmpty(bidItem.getImpid())) {
                                bid.put("impid", bidItem.getImpid());
                            }
                            bid.put("price", bidItem.getPrice());
                            if (!StringUtils.isEmpty(bidItem.getNurl())) {
                                bid.put("nurl", bidItem.getNurl());
                            }
                            if (!StringUtils.isEmpty(bidItem.getBurl())) {
                                bid.put("burl", bidItem.getBurl());
                            }
                            if (!StringUtils.isEmpty(bidItem.getLurl())) {
                                bid.put("lurl", bidItem.getLurl());
                            }
                            if (!StringUtils.isEmpty(bidItem.getAdm())) {
                                bid.put("adm", bidItem.getAdm());
                            }
                            if (!StringUtils.isEmpty(bidItem.getAdid())) {
                                bid.put("adid", bidItem.getAdid());
                            }
                            if (null != bidItem.getAdomain() && 0 != bidItem.getAdomain().length) {
                                bid.put("adomain", bidItem.getAdomain());
                            }
                            if (!StringUtils.isEmpty(bidItem.getBundle())) {
                                bid.put("bundle", bidItem.getBundle());
                            }
                            if (!StringUtils.isEmpty(bidItem.getIurl())) {
                                bid.put("iurl", bidItem.getIurl());
                            }
                            if (!StringUtils.isEmpty(bidItem.getCid())) {
                                bid.put("cid", bidItem.getCid());
                            }
                            if (!StringUtils.isEmpty(bidItem.getCrid())) {
                                bid.put("crid", bidItem.getCrid());
                            }
                            if (!StringUtils.isEmpty(bidItem.getTactic())) {
                                bid.put("tactic", bidItem.getTactic());
                            }
                            if (null != bidItem.getCat() && 0 != bidItem.getCat().length) {
                                bid.put("cat", bidItem.getCat());
                            }
                            if (null != bidItem.getAttr() && 0 != bidItem.getAttr().length) {
                                bid.put("attr", bidItem.getAttr());
                            }
                            if (!StringUtils.isEmpty(bidItem.getApi())) {
                                bid.put("api", bidItem.getApi());
                            }
                            if (!StringUtils.isEmpty(bidItem.getProtocol())) {
                                bid.put("protocol", bidItem.getProtocol());
                            }
                            if (!StringUtils.isEmpty(bidItem.getQagmediarating())) {
                                bid.put("qagmediarating", bidItem.getQagmediarating());
                            }
                            if (!StringUtils.isEmpty(bidItem.getLanguage())) {
                                bid.put("language", bidItem.getLanguage());
                            }
                            if (!StringUtils.isEmpty(bidItem.getDealid())) {
                                bid.put("dealid", bidItem.getDealid());
                            }
                            if (!StringUtils.isEmpty(bidItem.getW())) {
                                bid.put("w", bidItem.getW());
                            }
                            if (!StringUtils.isEmpty(bidItem.getH())) {
                                bid.put("h", bidItem.getH());
                            }
                            if (!StringUtils.isEmpty(bidItem.getWratio())) {
                                bid.put("wratio", bidItem.getWratio());
                            }
                            if (!StringUtils.isEmpty(bidItem.getHratio())) {
                                bid.put("hratio", bidItem.getHratio());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExp())) {
                                bid.put("exp", bidItem.getExp());
                            }
                            if (null != bidItem.getExt()) {
                                if (StringUtils.isEmpty(bidItem.getExt().getTargeturl())) {
                                    log.error("广告投放链接错误，{}", JSON.toJSONString(bidResponse));
                                    return "";
                                }
                                JSONObject bidExt = new JSONObject();
                                bidExt.put("admt", bidItem.getExt().getAdmt());
                                bidExt.put("adct", bidItem.getExt().getAdct());
                                if (!StringUtils.isEmpty(bidItem.getExt().getIcon())) {
                                    bidExt.put("icon", bidItem.getExt().getIcon());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getAdi())) {
                                    bidExt.put("adi", bidItem.getExt().getAdi());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getAdt())) {
                                    bidExt.put("adt", bidItem.getExt().getAdt());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getAds())) {
                                    bidExt.put("ads", bidItem.getExt().getAds());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getDan())) {
                                    bidExt.put("dan", bidItem.getExt().getDan());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getTargeturl())) {
                                    bidExt.put("targeturl", bidItem.getExt().getTargeturl());
                                }
                                if (!StringUtils.isEmpty(bidItem.getExt().getDeeplinkurl())) {
                                    bidExt.put("deeplinkurl", bidItem.getExt().getDeeplinkurl());
                                }
                                if (null != bidItem.getExt().getImptrackers() && 0 != bidItem.getExt().getImptrackers().length) {
                                    bidExt.put("imptrackers", bidItem.getExt().getImptrackers());
                                }
                                if (null != bidItem.getExt().getClicktrackers() && 0 != bidItem.getExt().getClicktrackers().length) {
                                    bidExt.put("clicktrackers", bidItem.getExt().getClicktrackers());
                                }
                                if (null != bidItem.getExt().getDownloadbegintrackers() && 0 != bidItem.getExt().getDownloadbegintrackers().length) {
                                    bidExt.put("downloadbegintrackers", bidItem.getExt().getDownloadbegintrackers());
                                }
                                if (null != bidItem.getExt().getDownloadendtrackers() && 0 != bidItem.getExt().getDownloadendtrackers().length) {
                                    bidExt.put("downloadendtrackers", bidItem.getExt().getDownloadendtrackers());
                                }
                                if (null != bidItem.getExt().getInstalledtrackers() && 0 != bidItem.getExt().getInstalledtrackers().length) {
                                    bidExt.put("installedtrackers", bidItem.getExt().getInstalledtrackers());
                                }
                                if (null != bidItem.getExt().getDeeplinktrackers() && 0 != bidItem.getExt().getDeeplinktrackers().length) {
                                    bidExt.put("deeplinktrackers", bidItem.getExt().getDeeplinktrackers());
                                }
                                bid.put("ext", bidExt);
                            }
                            bids.add(bid);
                        }
                        seatBid.put("bid", bids);
                        seatbids.add(seatBid);
                    }
                    map.put("seatbid", seatbids);
                    log.debug("返回广告：{}", JSON.toJSONString(map));
                    return JSON.toJSONString(map);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }
}
