package com.adexchange.adsponsor.advertiser;

import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.dto.adview.AVRequest;
import com.adexchange.adsponsor.dto.adview.AVResponse;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.util.WebUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class Adview {
    public BidResponse getAds(BidRequest request, String advertiserId, Integer timeOut) {
        BidResponse response = new BidResponse();
        response.setId(request.getId());
        try {
            //请求实例
            JSONObject avRequest = new JSONObject();
            avRequest.put("id", request.getId());
            avRequest.put("test", request.getTest());
            avRequest.put("at", request.getAt());
            avRequest.put("tmax", request.getTmax());
            avRequest.put("allimps", request.getAllimps());
            JSONArray imps = new JSONArray();
            for (BidRequest.BidRequestImp impItem : request.getImp()) {
                JSONObject imp = new JSONObject();
                imp.put("id", impItem.getId());
                imp.put("instl", impItem.getInstl());
                imp.put("tagid", impItem.getTagid());
                imp.put("bidfloor", impItem.getBidfloor());
                imp.put("bidfloorcur", impItem.getBidfloorcur());
                imp.put("banner", impItem.getBanner());
                Map<String, Object> impMap = BeanMap.create(impItem);
                if (null != impMap.get("impNative")) {
                    BidRequest.BidRequestImpNative impNative = (BidRequest.BidRequestImpNative) impMap.get("impNative");
                    BidRequest.BidRequestImpNativeRequest nativeRequest = JSON.parseObject(impNative.getRequest(),
                            BidRequest.BidRequestImpNativeRequest.class);
                    JSONObject avNative=new JSONObject();
                    avNative.put("request", JSON.toJSONString(nativeRequest.getNativeRequest()));
                    avNative.put("ver",impNative.getVer());
                    imp.put("native",avNative);
                }
                imps.add(imp);
            }
            avRequest.put("imp", imps);
            avRequest.put("app",request.getApp());
            avRequest.put("device",request.getDevice());

            // 请求广告
            log.info("开始请求Adview广告，参数：{}", JSON.toJSONString(avRequest));
            /** 获取当前系统时间*/
            long startTime = System.currentTimeMillis();
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://online.quickh5.com/api/public/bid?token=57db835396764b2387d6368816634211",
                    JSON.toJSONString(avRequest), timeOut);
            /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数*/
            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;
            // 返回实例
            if (responseResult.getResult() == WebResponseResult.ResultEnum.SUCCESS.getValue()) {
                AVResponse avResponse = JSON.parseObject(responseResult.getResponse(), AVResponse.class);

                log.info("请求ChangeMaker广告结束，耗时：{}，返回：{}", usedTime, responseResult.getResponse());
                if (null != avResponse.getSeatbid() && avResponse.getSeatbid().length > 0) {
                    BidResponse.SeatBid seatBid = new BidResponse.SeatBid();
                    List<BidResponse.Bid> bids = new ArrayList<>();
                    for (AVResponse.SeatBid seatBidItem : avResponse.getSeatbid()) {
                        for (AVResponse.Bid bidItem : seatBidItem.getBid()) {
                            BidResponse.Bid bid = new BidResponse.Bid();
                            BidResponse.BidExt bidExt = new BidResponse.BidExt();
                            bid.setId(UUID.randomUUID().toString());
                            bid.setImpid(request.getImp()[0].getId());
                            bid.setPrice(0F);
                            bid.setAdid(advertiserId);
                            bid.setCid(bidItem.getCid());
                            bid.setCrid(bidItem.getCrid());
                            bid.setW(bidItem.getW());
                            bid.setH(bidItem.getH());
                            bid.setBundle(bidItem.getBundle());
                            bid.setNurl(bidItem.getNurl());
                            bid.setAdm(bidItem.getAdm());
                            if (null != bidItem.getExt()) {
                                bidExt.setAdmt(bidItem.getExt().getAdmt());
                                bidExt.setAdi(bidItem.getExt().getAdi());
                                bidExt.setAdt(bidItem.getExt().getAdt());
                                bidExt.setAds(bidItem.getExt().getAds());
                                bidExt.setDan(bidItem.getExt().getDan());
                                bidExt.setTargeturl(bidItem.getExt().getTargeturl());
                                bidExt.setDeeplinkurl(bidItem.getExt().getDeeplinkurl());
                                bidExt.setImptrackers(bidItem.getExt().getImptrackers());
                                bidExt.setClicktrackers(bidItem.getExt().getClicktrackers());
                                bidExt.setDownloadbegintrackers(bidItem.getExt().getDownloadbegintrackers());
                                bidExt.setDownloadendtrackers(bidItem.getExt().getDownloadendtrackers());
                                bidExt.setInstalledtrackers(bidItem.getExt().getInstalledtrackers());
                                bidExt.setDeeplinktrackers(bidItem.getExt().getDeeplinktrackers());
                                bidExt.setAdct(bidItem.getExt().getAdct());
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


                                AVResponse.NativeResponse avNativeResponse = JSON.parseObject(bidItem.getAdm(),
                                        AVResponse.NativeResponse.class);
                                for (BidRequest.NativeRequestAsset asset : nativeRequest.getAssets()) {
                                    BidResponse.NativeResponseAsset responseAsset = new BidResponse.NativeResponseAsset();
                                    responseAsset.setId(asset.getId());
                                    responseAsset.setRequired(asset.getRequired());
                                    if (null != avNativeResponse.getAssets()) {
                                        for (AVResponse.NativeResponseAsset assetItem : avNativeResponse.getAssets()) {
                                            // 标题
                                            if (null != asset.getTitle() && null != assetItem.getTitle()) {
                                                BidResponse.NativeResponseAssetTitle title =
                                                        new BidResponse.NativeResponseAssetTitle();
                                                title.setText(assetItem.getTitle().getText());
                                                title.setLen(assetItem.getTitle().getLen());
                                                responseAsset.setTitle(title);
                                                nativeResponseAssets.add(responseAsset);
                                            }
                                            // 图片
                                            if (null != asset.getImg() && null != assetItem.getImg()) {
                                                if (asset.getImg().getType() == assetItem.getImg().getType()) {
                                                    BidResponse.NativeResponseAssetImage image =
                                                            new BidResponse.NativeResponseAssetImage();
                                                    image.setType(assetItem.getImg().getType());
                                                    image.setW(assetItem.getImg().getW());
                                                    image.setH(assetItem.getImg().getH());
                                                    image.setUrl(assetItem.getImg().getUrl());
                                                    responseAsset.setImg(image);
                                                    nativeResponseAssets.add(responseAsset);
                                                }
                                            }
                                            if (null != asset.getData() && null != assetItem.getData()) {
                                                BidResponse.NativeResponseAssetData data = new BidResponse.NativeResponseAssetData();
                                                data.setValue(assetItem.getData().getValue());
                                                data.setLen(assetItem.getData().getLen());
                                                responseAsset.setData(data);
                                                nativeResponseAssets.add(responseAsset);
                                            }
                                        }
                                    }
                                }
                                //集合转数组
                                BidResponse.NativeResponseAsset[] responseAssetArray = new
                                        BidResponse.NativeResponseAsset[nativeResponseAssets.size()];
                                nativeResponseAssets.toArray(responseAssetArray);
                                nativeResponse.setAssets(responseAssetArray);
                                if(null!=avNativeResponse.getLink()){
                                    nativeResponseAssetLink.setUrl(avNativeResponse.getLink().getUrl());
                                    nativeResponseAssetLink.setClicktrackers(avNativeResponse.getLink().getClicktrackers());
                                }
                                nativeResponse.setImptrackers(avNativeResponse.getImptrackers());
                                nativeResponse.setLink(nativeResponseAssetLink);

                                BidResponse.BidNativeResponse bidNativeResponse = new BidResponse.BidNativeResponse();
                                bidNativeResponse.setNativeResponse(nativeResponse);
                                bid.setAdm(JSON.toJSONString(bidNativeResponse));
                                bidExt.setAdmt(6);
                            }

                            bid.setExt(bidExt);
                            bids.add(bid);
                        }
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
