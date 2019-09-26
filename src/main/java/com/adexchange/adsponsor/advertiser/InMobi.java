package com.adexchange.adsponsor.advertiser;

import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.dto.inmobi.IMResponse;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.util.WebUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
public class InMobi {
    public BidResponse getAds(BidRequest request, String advertiserId, Integer timeOut) {
        BidResponse response = new BidResponse();
        response.setId(request.getId());
        try {
            // 广告位，由广告主提供分配
            Map<String, String> adSlots = new HashMap<String, String>();
            adSlots.put("20", "1555374654320"); // Banner Android
            adSlots.put("23", "1555374654320"); // 信息流 Android
            adSlots.put("21", "1556093945360"); // 插屏 Android
            adSlots.put("22", "1554971028081"); // 全屏 Android
            adSlots.put("10", "1555708534794"); // Banner IOS
            adSlots.put("13", "1555708534794"); // 信息流 IOS
            adSlots.put("12", "1568555366156"); // 全屏 IOS
            adSlots.put("11", "1557787096421"); // 插屏 IOS
            String adSlotId = "";
            // 请求实例
            JSONObject imRequest = new JSONObject();
            String adSlotKey = new StringBuffer().append(request.getRecord().getOs()).append(request.getRecord().getAdmode()).toString();
            adSlotId = adSlots.get(adSlotKey);

            JSONObject app = new JSONObject();
            adSlotId = adSlots.get(adSlotKey);
            app.put("id", adSlotId);
            app.put("bundle", request.getRecord().getOs() == 1 ? "com.ljhy.wcdzz" : "com.tencent.tmgp.wcdzzqqqq");
            imRequest.put("app", app);

            JSONObject imp = new JSONObject();
            imp.put("secure", 0);

            JSONObject jnative = new JSONObject();
            jnative.put("layout", 0);
            jnative.put("trackertype", "url_ping");
            imp.put("native", jnative);

            JSONObject impExt = new JSONObject();
            impExt.put("ads", 1);

            imp.put("ext", impExt);
            imRequest.put("imp", imp);

            JSONObject device = new JSONObject();
            if (!StringUtils.isEmpty(request.getDevice().getOs())) {
                device.put("os", request.getDevice().getOs().toLowerCase());
            }
            if (null != request.getDevice().getExt()) {
                device.put("os", request.getDevice().getExt().getIdfa());
            }
            device.put("gpid", "");
            device.put("md5_imei", request.getDevice().getDidmd5());
            device.put("sha1_imei", request.getDevice().getDidsha1());
            device.put("o1", request.getDevice().getDidsha1());
            device.put("um5", request.getDevice().getDpidmd5());
            device.put("connectiontype", request.getDevice().getConnectiontype());
            if (!StringUtils.isEmpty(request.getDevice().getUa())) {
                device.put("ua", request.getDevice().getUa());
            }
            if (!StringUtils.isEmpty(request.getDevice().getIp())) {
                device.put("ip", request.getDevice().getIp());
            }
            if (!StringUtils.isEmpty(request.getDevice().getOsv())) {
                device.put("osv", request.getDevice().getOsv());
            }
            if (!StringUtils.isEmpty(request.getDevice().getModel())) {
                device.put("model", request.getDevice().getModel());
            }
            //可选参数
            if (null != request.getDevice().getGeo()) {
                JSONObject geo = new JSONObject();
                geo.put("lat", request.getDevice().getGeo().getLat());
                geo.put("lon", request.getDevice().getGeo().getLon());
                geo.put("accu", request.getDevice().getGeo().getAccuracy());
                geo.put("type", request.getDevice().getGeo().getType());
                geo.put("city", request.getDevice().getGeo().getCity());
                geo.put("country", request.getDevice().getGeo().getCountry());
                device.put("geo", geo);
            }
            device.put("carrier", request.getDevice().getMccmnc());
            JSONObject regs = new JSONObject();
            regs.put("coppa", 0);
            JSONObject regsExt = new JSONObject();
            regsExt.put("gdpr", 0);
            regs.put("ext", regsExt);
            device.put("regs", regs);

            imRequest.put("device", device);

            JSONObject ext = new JSONObject();
            ext.put("responseformat", "json");
            ext.put("externalSupported", true);
            imRequest.put("ext", ext);

            // 请求广告
            log.info("开始请求InMobi广告，参数：{}", JSON.toJSONString(imRequest));
            /** 获取当前系统时间*/
            long startTime = System.currentTimeMillis();
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://api.w.inmobi.cn/showad/v3.1",
                    JSON.toJSONString(imRequest));
            /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数*/
            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;

            // 返回实例
            if (responseResult.getResult() == WebResponseResult.ResultEnum.SUCCESS.getValue()) {
                IMResponse imResponse = JSON.parseObject(responseResult.getResponse(), IMResponse.class);

                log.info("请求InMobi广告结束，耗时：{}，返回：{}", usedTime, responseResult.getResponse());
                // 成功
                if (null != imResponse.getAds() && imResponse.getAds().length > 0) {
                    BidResponse.SeatBid seatBid = new BidResponse.SeatBid();
                    List<BidResponse.Bid> bids = new ArrayList<>();
                    for (IMResponse.IMAd ad : imResponse.getAds()) {
                        BidResponse.Bid bid = new BidResponse.Bid();
                        BidResponse.BidExt bidExt = new BidResponse.BidExt();
                        bid.setId(UUID.randomUUID().toString());
                        bid.setImpid(request.getImp()[0].getId());
                        bid.setPrice(0F);
                        bid.setAdid(advertiserId);
                        bid.setCid(adSlotId);
                        bidExt.setAdmt(2);

                        IMResponse.PubContent pubContent = ad.getPubContent();
                        if (null != pubContent.getIcon()) {
                            bidExt.setIcon(pubContent.getIcon().getUrl());
                        }
                        bidExt.setAdt(pubContent.getTitle());
                        bidExt.setAds(pubContent.getDescription());
                        bidExt.setTargeturl(ad.getLandingPage());
                        bidExt.setDeeplinkurl(pubContent.getLandingURL());
                        bidExt.setDan(pubContent.getCta());

                        if (null != pubContent.getScreenshots()) {
                            bid.setW(pubContent.getScreenshots().getWidth());
                            bid.setH(pubContent.getScreenshots().getHeight());
                            bidExt.setAdi(pubContent.getScreenshots().getUrl());
                        }
                        if (ad.getIsApp()) {
                            bidExt.setAdct(2);
                        } else {
                            bidExt.setAdct(1);
                        }
                        // 监测链接
                        Dictionary<String, IMResponse.IMUrl> dc = ad.getEventTracking();
                        //客户端下载完成的链接
                        if (null != dc.get("1")) {
                           bidExt.setDownloadendtrackers(dc.get("1").urls);
                        }
                        //客户端产生点击的链接
                        if (null != dc.get("8")) {
                            bidExt.setClicktrackers(dc.get("8").urls);
                        }
                        //客户端产生曝光的链接
                        if (null != dc.get("18")) {
                            bidExt.setImptrackers(dc.get("18").urls);
                        }
                        // 原生广告
                        if (request.getImp()[0].getInstl() == 3) {
                            //原生广告请求信息
                            BidRequest.BidRequestImpNativeRequest bidRequestImpNativeRequest =
                                    JSON.parseObject(request.getImp()[0].getImpNative().getRequest(),
                                            BidRequest.BidRequestImpNativeRequest.class);
                            BidRequest.NativeRequest nativeRequest = bidRequestImpNativeRequest.getNativeRequest();

                            //原生广告返回实体
                            BidResponse.NativeResponse nativeResponse = new BidResponse.NativeResponse();
                            List<BidResponse.NativeResponseAsset> nativeResponseAssets =
                                    new ArrayList<>();
                            BidResponse.NativeResponseAssetLink nativeResponseAssetLink =
                                    new BidResponse.NativeResponseAssetLink();
                            for (BidRequest.NativeRequestAsset asset : nativeRequest.getAssets()) {
                                BidResponse.NativeResponseAsset responseAsset = new BidResponse.NativeResponseAsset();
                                responseAsset.setId(asset.getId());
                                responseAsset.setRequired(asset.getRequired());
                                // 标题
                                if (null != asset.getTitle()) {
                                    BidResponse.NativeResponseAssetTitle title =
                                            new BidResponse.NativeResponseAssetTitle();
                                    title.setText(pubContent.getTitle());
                                    title.setLen(pubContent.getTitle().length());
                                    responseAsset.setTitle(title);
                                }
                                // 图片
                                if (null != asset.getImg()) {
                                    if(null!=pubContent.getScreenshots()){
                                        BidResponse.NativeResponseAssetImage image =
                                                new BidResponse.NativeResponseAssetImage();
                                        image.setType(asset.getImg().getType());
                                        image.setW(pubContent.getScreenshots().getWidth());
                                        image.setH(pubContent.getScreenshots().getHeight());
                                        image.setUrl(pubContent.getScreenshots().getUrl());
                                        responseAsset.setImg(image);
                                    }
                                }
                                if (null != asset.getData()) {
                                    BidResponse.NativeResponseAssetData data = new BidResponse.NativeResponseAssetData();
                                    data.setValue(pubContent.getDescription());
                                    data.setLen(pubContent.getDescription().length());
                                    responseAsset.setData(data);
                                }
                                nativeResponseAssets.add(responseAsset);
                            }
                            //集合转数组
                            BidResponse.NativeResponseAsset[] responseAssetArray = new
                                    BidResponse.NativeResponseAsset[nativeResponseAssets.size()];
                            nativeResponseAssets.toArray(responseAssetArray);
                            nativeResponse.setAssets(responseAssetArray);
                            nativeResponseAssetLink.setUrl(ad.getLandingPage());
                            //客户端产生点击的链接
                            if (null != dc.get("8")) {
                                nativeResponseAssetLink.setClicktrackers(dc.get("8").urls);
                            }
                            //客户端产生曝光的链接
                            if (null != dc.get("18")) {
                                nativeResponse.setImptrackers(dc.get("18").urls);
                            }
                            nativeResponse.setLink(nativeResponseAssetLink);

                            BidResponse.BidNativeResponse bidNativeResponse = new BidResponse.BidNativeResponse();
                            bidNativeResponse.setNativeResponse(nativeResponse);
                            bid.setAdm(JSON.toJSONString(bidNativeResponse));
                            bidExt.setAdmt(6);
                        }
                        bid.setExt(bidExt);
                        bids.add(bid);
                    }
                    BidResponse.Bid[] bidArray = new BidResponse.Bid[bids.size()];
                    bids.toArray(bidArray);
                    seatBid.setBid(bidArray);
                    //原币种返回
                    response.setCur(request.getImp()[0].getBidfloorcur());

                    BidResponse.SeatBid[] seatBidArray = new BidResponse.SeatBid[]{seatBid};
                    response.setSeatbid(seatBidArray);
                    response.setNbr(200); // 成功
                }
            }

        } catch (Exception e) {
            response.setNbr(1);
            log.error(e.getMessage());
        }
        return response;
    }
}
