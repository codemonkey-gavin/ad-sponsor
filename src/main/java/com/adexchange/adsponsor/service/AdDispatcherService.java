package com.adexchange.adsponsor.service;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

@Slf4j
@Service
public class AdDispatcherService {
    public BidResponse getAds(BidRequest request, String advertiserId) {
        Properties pro = new Properties();
        try {
            ClassPathResource classPathResource = new ClassPathResource("ads.properties");
            InputStream inputStream = classPathResource.getInputStream();
            pro.load(inputStream);
            inputStream.close();
            String advertiserName = pro.getProperty(advertiserId);
            if (!StringUtils.isEmpty(advertiserName)) {
                Class c = Class.forName(advertiserName);
                Constructor constructor = c.getConstructor();
                Object object = constructor.newInstance();
                Method method = c.getMethod("getAds", BidRequest.class, String.class, Integer.class);
                Object response = method.invoke(object, request, advertiserId, 300);
                return (BidResponse) response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
