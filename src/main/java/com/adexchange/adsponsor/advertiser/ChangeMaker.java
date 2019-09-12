package com.adexchange.adsponsor.advertiser;

import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.dto.changemaker.CMResponse;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.util.WebUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ChangeMaker {
    public BidResponse getAds(BidRequest request, int timeOut) {
        BidResponse response = new BidResponse(request.getId(), 0);
        try {
            // 请求实例
            JSONObject cmRequest = new JSONObject();
            cmRequest.put("n", 1);
            cmRequest.put("apv", "1.0.6");
            cmRequest.put("bid", request.getId());
            cmRequest.put("aid", "A117403000001"); // 广告位，由广告主提供分配
            for (BidRequest.BidRequestImp imp : request.getImp()) {
                switch (imp.getInstl()) {
                    case 0:
                        cmRequest.put("adt", 1);
                        break;
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
                if (null != imp.getIframebuster()) {
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
                cmRequest.put("cootype", 2); // 0 cps  1 固定价格  2 rtb竞价
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
                        if (null != request.getDevice().getCarrier()) {
                            terminalInfo.put("si", request.getDevice().getCarrier());
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
            /** 获取当前系统时间*/
            long startTime = System.currentTimeMillis();
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://testapi.i-changemaker.com:9999/105003",
                    JSON.toJSONString(cmRequest), timeOut);
            /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数*/
            long endTime = System.currentTimeMillis();
            long usedTime = endTime - startTime;
            System.out.println(usedTime);

            // 返回实例
            if (responseResult.getResult() == WebResponseResult.ResultEnum.SUCCESS.getValue()) {
                CMResponse cmResponse = JSON.parseObject(responseResult.getResponse(), CMResponse.class);
                // 成功
                if (cmResponse.getRcd() == 1) {
                    BidResponse.Bid bid = new BidResponse.Bid();
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
