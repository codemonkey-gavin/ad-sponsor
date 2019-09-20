package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.service.AdDispatcherService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class PublicController extends BaseController {

    @Autowired
    private AdDispatcherService adDispatcherService;

    @RequestMapping(value = "/bid/{token}/{advertiserId}", method = RequestMethod.POST)
    public Map<String, Object> openRTB(@PathVariable("token") String token,
                                       @PathVariable("advertiserId") String advertiserId,
                                       @RequestBody BidRequest bidRequest) {

        Map<String, Object> map = new HashMap<String, Object>();
        //参数验证
        if (null == bidRequest.getImp()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return map;
        }
        if (null == bidRequest.getDevice()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return map;
        }
        BidRequest.BidRequestRecord record = new BidRequest.BidRequestRecord();
        if (StringUtils.isEmpty(bidRequest.getDevice().getOs())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return map;
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

        BidResponse bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);

        // 获取广告成功
        if (null != bidResponse && bidResponse.getNbr() == 200) {
            map = BeanMap.create(bidResponse);
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return map;
    }
}
