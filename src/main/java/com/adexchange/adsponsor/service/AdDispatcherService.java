package com.adexchange.adsponsor.service;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class AdDispatcherService {
    public BidResponse getAds(BidRequest request, String advertiserId) {
        try {
            Properties pro = new Properties();
            ClassPathResource classPathResource = new ClassPathResource("ads.properties");
            InputStream inputStream = classPathResource.getInputStream();
            pro.load(inputStream);
            inputStream.close();
            String advertiserName = pro.getProperty(advertiserId);
            if (!StringUtils.isEmpty(advertiserName)) {
                Class<?>  cls = Class.forName(advertiserName);
                Method method = cls.getMethod("getAds", BidRequest.class, String.class, Integer.class);
                Object response = method.invoke(cls.newInstance(), request, advertiserId, 200);
                return (BidResponse) response;
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }

//        try {
//            return changeMaker.getAds(request, advertiserId, 200);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return null;
    }
}
