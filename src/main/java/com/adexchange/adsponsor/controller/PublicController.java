package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.service.AdDispatcherService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class PublicController {
    private final static String ERROR_MESSAGE = "Task error";
    private final static String TIME_MESSAGE = "Task timeout";
    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private AdDispatcherService adDispatcherService;

    @RequestMapping(value = "/bid/{token}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest,
                          HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        //参数验证
        if (null == bidRequest.getImp()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        if (null == bidRequest.getDevice()) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        BidRequest.BidRequestRecord record = new BidRequest.BidRequestRecord();
        if (StringUtils.isEmpty(bidRequest.getDevice().getOs())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
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

        String advertiserId = token;
        if (token == "582241fcec6842beae98d27f9a51d8da") {
            advertiserId = "10002";
        }
        try {
            String bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);
            if (StringUtils.isEmpty(bidResponse)) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            return bidResponse;
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
