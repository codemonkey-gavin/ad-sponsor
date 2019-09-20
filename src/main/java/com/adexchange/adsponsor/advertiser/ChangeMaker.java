package com.adexchange.adsponsor.advertiser;

import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.dto.changemaker.CMResponse;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.util.WebUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ChangeMaker {
    public BidResponse getAds(BidRequest request, String advertiserId, Integer timeOut) {
        BidResponse response = new BidResponse();
        response.setId(request.getId());

        try {

            // 广告位，由广告主提供分配
            Map<String, String> adSlots = new HashMap<String, String>();
            adSlots.put("20", "A117401000002"); // Banner Android
            adSlots.put("23", "A117401100001"); // 信息流 Android
            adSlots.put("21", "A117402000001"); // 插屏 Android
            adSlots.put("22", "A117403000001"); // 全屏 Android
            adSlots.put("10", "S117401000001"); // Banner IOS
            adSlots.put("13", "S117401100001"); // 信息流 IOS
            adSlots.put("12", "S117403000001"); // 全屏 IOS
            adSlots.put("11", "S117402000001"); // 插屏 IOS
            String adSlotId = "";
            // 请求实例
            JSONObject cmRequest = new JSONObject();
            cmRequest.put("n", 1);
            cmRequest.put("apv", "1.0.6");
            cmRequest.put("bid", request.getId());
            for (BidRequest.BidRequestImp imp : request.getImp()) {
                String adSlotKey =
                        new StringBuffer().append(request.getRecord().getOs()).append(request.getRecord().getAdmode()).toString();
                adSlotId = adSlots.get(adSlotKey);
                cmRequest.put("aid", adSlotId);
                switch (imp.getInstl()) {
                    case 1:
                        cmRequest.put("adt", 2);
                        break;
                    case 2:
                        cmRequest.put("adt", 3);
                        break;
                    case 3:
                        cmRequest.put("adt", 7);
                        break;
                    case 4:
                        cmRequest.put("adt", 6);
                        break;
                    default:
                        cmRequest.put("adt", 1);
                }
                if (null != imp.getBanner()) {
                    cmRequest.put("adsw", imp.getBanner().getW());
                    cmRequest.put("adsh", imp.getBanner().getH());
                }
                if (null != imp.getImpNative()) {
                    BidRequest.BidRequestImpNativeRequest bidRequestImpNativeRequest =
                            JSON.parseObject(imp.getImpNative().getRequest(),
                                    BidRequest.BidRequestImpNativeRequest.class);
                    BidRequest.NativeRequest nativeRequest = bidRequestImpNativeRequest.getNativeRequest();
                    for (BidRequest.NativeRequestAsset asset : nativeRequest.getAssets()) {
                        if (null != asset.getImg()) {
                            cmRequest.put("adsw", asset.getImg().getW());
                            cmRequest.put("adsh", asset.getImg().getH());
                            break;
                        }
                    }
                }
                cmRequest.put("cootype", 1); // 0 cps  1 固定价格  2 rtb竞价
                cmRequest.put("bidprice", imp.getBidfloor());

                // 设备信息
                JSONObject terminalInfo = new JSONObject();
                if (null != request.getDevice()) {
                    terminalInfo.put("bn", request.getDevice().getMake());
                    terminalInfo.put("hm", request.getDevice().getMake());
                    terminalInfo.put("ht", request.getDevice().getModel());
                    if (null != request.getDevice().getOs()) {
                        switch (request.getDevice().getOs()) {
                            case "ios":
                                terminalInfo.put("os", 1);
                                break;
                            case "android":
                                terminalInfo.put("os", 0);
                                break;
                            default:
                                terminalInfo.put("os", 3);
                        }
                    }
                    if (null != request.getDevice().getOsv()) {
                        terminalInfo.put("ov", request.getDevice().getOsv());
                        terminalInfo.put("sw", request.getDevice().getW());
                        terminalInfo.put("sh", request.getDevice().getH());
                        terminalInfo.put("ch", "p1174@tl");
                        if (null != request.getDevice().getExt()) {
                            terminalInfo.put("ei", request.getDevice().getExt().getImei());
                            terminalInfo.put("idfa", request.getDevice().getExt().getIdfa());
                            terminalInfo.put("andid", request.getDevice().getExt().getAndroidid());
                            terminalInfo.put("mac", request.getDevice().getExt().getMac());
                        }
                        terminalInfo.put("oid", request.getDevice().getIfa());
                        terminalInfo.put("ip", request.getDevice().getIp());
                        terminalInfo.put("ua", request.getDevice().getUa());
                        terminalInfo.put("dpi", request.getDevice().getPxratio());
                        terminalInfo.put("smd", request.getDevice().getPpi());
                        if (null != request.getDevice().getGeo()) {
                            terminalInfo.put("lon", request.getDevice().getGeo().getLon());
                            terminalInfo.put("lat", request.getDevice().getGeo().getLat());
                        }
                        if (null != request.getApp()) {
                            terminalInfo.put("pkg", request.getApp().getBundle());
                            terminalInfo.put("apnm", request.getApp().getName());
                            terminalInfo.put("dplink", 1);
                        }
                        if (null != request.getDevice().getMccmnc()) {
                            terminalInfo.put("si", request.getDevice().getMccmnc());
                        }
                        switch (request.getDevice().getConnectiontype()) {
                            case 0:
                            case 1:
                            case 3:
                                terminalInfo.put("nt", 4);
                                break;
                            case 2:
                                terminalInfo.put("nt", 3);
                                break;
                            case 4:
                                terminalInfo.put("nt", 1);
                                break;
                            case 5:
                                terminalInfo.put("nt", 2);
                                break;
                            case 6:
                                terminalInfo.put("nt", 5);
                                break;
                        }
                    }
                }

                cmRequest.put("ti", terminalInfo);
                break; // 目前只支持一个广告
            }

            // 请求广告
            log.info("开始请求ChangeMaker广告，参数：{}", JSON.toJSONString(cmRequest));
            /** 获取当前系统时间*/
            long startTime = System.currentTimeMillis();
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://tling.bigdata-hub.cn:6688/105003",
                    JSON.toJSONString(cmRequest), timeOut);
            /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数*/
            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;
            log.info("请求ChangeMaker广告结束，耗时：{}，返回：{}", usedTime, JSON.toJSONString(responseResult));
            // 返回实例
            if (responseResult.getResult() == WebResponseResult.ResultEnum.SUCCESS.getValue()) {
                CMResponse cmResponse = JSON.parseObject(responseResult.getResponse(), CMResponse.class);
                // 成功
                if (cmResponse.getRcd() == 1) {
                    response.setNbr(200);
                    BidResponse.SeatBid seatBid = new BidResponse.SeatBid();
                    List<BidResponse.Bid> bids = new ArrayList<>();
                    for (CMResponse.Ad ad : cmResponse.getAd()) {
                        BidResponse.Bid bid = new BidResponse.Bid();
                        BidResponse.BidExt bidExt = new BidResponse.BidExt();
                        bid.setId(UUID.randomUUID().toString());
                        bid.setImpid(request.getImp()[0].getId());
                        bid.setPrice(0F);
                        bid.setAdid(advertiserId);
                        bid.setCid(adSlotId);
                        bid.setW(ad.getWidth());
                        bid.setH(ad.getHeight());
                        bid.setBundle(ad.getPack());
                        bid.setNurl(ad.getWurl());
                        switch (ad.getAt()) {
                            case 1:
                                bidExt.setAdmt(1);
                                break;
                            case 2:
                                bidExt.setAdmt(2);
                                break;
                            case 3:
                                bidExt.setAdmt(3);
                                break;
                            case 5:
                                bidExt.setAdmt(4);
                                break;
                            case 7:
                                bidExt.setAdmt(5);
                                break;
                        }
                        if (ad.getAt() == 5) {
                            bid.setAdm(ad.getAdl());
                        } else {
                            bidExt.setAdi(ad.getAdl());
                            bidExt.setAdt(ad.getAti());
                            bidExt.setAds(ad.getAtx());
                            bidExt.setDan(ad.getAppname());
                            bidExt.setTargeturl(ad.getClk());
                            bidExt.setDeeplinkurl(ad.getDeeplink());
                            bidExt.setImptrackers(ad.getEs());
                            bidExt.setClicktrackers(ad.getEc());
                            bidExt.setDownloadbegintrackers(ad.getStd());
                            bidExt.setDownloadendtrackers(ad.getDcp());
                            bidExt.setInstalledtrackers(ad.getItal());
                            bidExt.setDeeplinktrackers(ad.getDurls());
                        }
                        switch (ad.getAct()) {
                            case 3:
                            case 4:
                                bidExt.setAdct(2);
                                break;
                            default:
                                bidExt.setAdct(1);
                                break;
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
                                    title.setText(ad.getAti());
                                    title.setLen(ad.getAti().length());
                                    responseAsset.setTitle(title);
                                }
                                // 图片
                                if (null != asset.getImg()) {
                                    BidResponse.NativeResponseAssetImage image =
                                            new BidResponse.NativeResponseAssetImage();
                                    image.setType(asset.getImg().getType());
                                    image.setW(ad.getWidth());
                                    image.setH(ad.getHeight());
                                    image.setUrl(ad.getAdl());
                                    responseAsset.setImg(image);
                                }
                                if (null != asset.getData()) {
                                    BidResponse.NativeResponseAssetData data = new BidResponse.NativeResponseAssetData();
                                    data.setValue(ad.getAtx());
                                    data.setLen(ad.getAtx().length());
                                    responseAsset.setData(data);
                                }
                                nativeResponseAssets.add(responseAsset);
                            }
                            //集合转数组
                            BidResponse.NativeResponseAsset[] responseAssetArray = new
                                    BidResponse.NativeResponseAsset[nativeResponseAssets.size()];
                            nativeResponseAssets.toArray(responseAssetArray);
                            nativeResponse.setAssets(responseAssetArray);
                            nativeResponseAssetLink.setUrl(ad.getClk());
                            nativeResponseAssetLink.setClicktrackers(ad.getEc());
                            nativeResponse.setImptrackers(ad.getEs());
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
                }
            }
        } catch (Exception e) {
            response.setNbr(1);
            log.error(e.getMessage());
        }
        return response;
    }
}
