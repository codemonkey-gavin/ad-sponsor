package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.service.AdDispatcherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class PublicController {

    @Autowired
    private AdDispatcherService adDispatcherService;

    @RequestMapping(value = "/bid/{token}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest, HttpServletResponse response) {
        try {
            long startTime = System.currentTimeMillis();
            //参数验证
            if (null == bidRequest.getImp()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            if (null == bidRequest.getDevice()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            BidRequest.BidRequestRecord record = new BidRequest.BidRequestRecord();
            if (StringUtils.isEmpty(bidRequest.getDevice().getOs())) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
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

            BidResponse bidResponse = null;
            Integer i = token.indexOf("@");
            if (i > 0) {
                String[] array = token.split("@");
                String advertiserId = array[1];
                bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);
            } else {
                //log.debug(JSON.toJSONString(bidRequest));
                bidResponse = adDispatcherService.getAds(bidRequest, "10002");
            }

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
                long endTime = System.currentTimeMillis();
                long usedTime = endTime - startTime;
                if (i > 0) {
                    log.debug("token:{}，耗时：{}，广告：{}", token, usedTime, JSON.toJSONString(map));
                } else {
                    log.trace("token:{}，耗时：{}，广告：{}", token, usedTime, JSON.toJSONString(map));
                }
                return JSON.toJSONString(map);
            } else {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "";
        }
    }

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String test() {
        return "Hello World!";
    }
}
