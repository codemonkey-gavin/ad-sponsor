package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PublicController {

    @RequestMapping(value = "/bid/{token}/{advertiserid}", method = RequestMethod.POST)
    public Map<String, Object> openRTB(@PathVariable("token") String token,
                                       @PathVariable("advertiserid") Long advertiserid,
                                       @RequestBody BidRequest bidRequest) {
        System.out.println(token);
        System.out.println(advertiserid);
        System.out.println(JSON.toJSONString(bidRequest));
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }
}
