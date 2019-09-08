package com.adexchange.adsponsor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class PublicController {

    @RequestMapping(value = "/bid", method = RequestMethod.POST)
    public Map<String, Object> openRTB() {
        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }
}
