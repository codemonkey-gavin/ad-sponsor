package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.service.AdDispatcherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/api")
public class PublicController {

    @Autowired
    private AdDispatcherService adDispatcherService;

    @RequestMapping(value = "/bid/{token}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest, HttpServletResponse response) {
        try {
            long startTime = System.currentTimeMillis();
            //参数验证
            if (null == bidRequest.getImp()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            if (null == bidRequest.getDevice()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
            BidRequest.BidRequestRecord record = new BidRequest.BidRequestRecord();
            if (StringUtils.isEmpty(bidRequest.getDevice().getOs())) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
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

            BidResponse bidResponse = null;
            Integer i = token.indexOf("@");
            if (i > 0) {
                String[] array = token.split("@");
                String advertiserId = array[1];
                bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);
            } else {
                //log.debug(JSON.toJSONString(bidRequest));
                String json = "{\"cur\":\"CNY\",\"id\":\"31272.001-42.w56qp9.456849520.py772r.c239.620056.妈妈宝典-Android-Banner\",\"seatbid\":[{\"bid\":[{\"ext\":{\"adt\":\"高洁丝\",\"ads\":\"点击了解更多\",\"clicktrackers\":[\"http://et.w.inmobi.cn/c.asm/HDboppmy1PjBBRbKzAsUWBTIAhWqDBsEWCAoODhjNDg4ZWM5N2I3N2NjMDRmNjUyMTJjNzYyMzdhNDIzOGUzZGI2Mx4gNzJiYjVhMTJiMzBmMTVlNzVlNjhlMWI3ODUxZjhjNTAAIGI5OTkzZDFlMTMwMTIxMWEzZDZmYTQ5MmFmYTNjMDgyBCg2OGVhMDgxZDQzM2M4ODNmYzNiODA3MzExMzE3NDc2OTZhYTM3NzMzHBUEFRgVAhXsBBUCJQAoA2RpcgATABwXz2bV52orPkAX4umVsgxRXkAAFQAYKFkyOXRMbXBqWDJsdWRHVnlMblJ5WVc1emJHRjBhVzl1ZEhKcGNBfn4Wyv33AhwcFoDA6JaAguT5qgEWlf3__9-43v8FAAAVHhcAAAAAAABUQCQAHBIZBQAWyv33AhgDVVNEFryfrcu5Whn1HhS8A4Ibvj6YToJ9rKwBrqwBtKwBuK0BgK8B0rsB1rsBkMsBwu0Biu8B0vAB0IkC0okC5pkCjpoCmpoC4JoC4NcCvP0C3IIDtocDuIcD8qUDoJMEIRhNDAABCwABAAAAIDkwYTkzOThkZWU1OTRlZGRiYzczMzgwMzM5ZDhkNzYzCgACAAAAAAAXOKYABAACQAThR64UeuEKAAQAAAAAAC7_ZQA4IgQAAUBUAAAAAAAABAACQAUAAAAAAAAMAAMIAAEAAAABAAAYbQMAAQAIAAIAAAANBgAFAoAGAAYEcAsABwAAAAUzLjEuMAsACAAAAANBUFAIAAkAAAABAgAKAAgACwAE178EAAxAAMzMzMzMzQgADQAOi3AIAA4AAHF4CwAPAAAAD0RFUklWRURfTEFUX0xPTgAYpAEKAAFGUnlxH3CugwsAAgAAAIwLAAEAAAAcY29tLmpjX2ludGVyLnRyYW5zbGF0aW9udHJpcAMAAgACAAQBAgAFAAYABhAQCwAHAAAABFJUQkQIAA4AAAAACwAPAAAABzMyMFg1NjgGABAAAAsAEQAAACA5MGE5Mzk4ZGVlNTk0ZWRkYmM3MzM4MDMzOWQ4ZDc2MwoAEgAAAAAAqUvGAAYABAAAAAAYmwI2vJ-ty7laqDhBNFlDM3pQejMwZU1zVCtZWSszQUVSR3BiVnlmSmtTVGlpZjJSV3RzU0lWdWt0M3Z1QmZPWlE9PRgGTkFUSVZFHDwcFoDA6JaAguT5qgEWlf3__9-43v8FABaGuoX3o9y80owBIgAAOfUegK8Bgn2CG4rvAY6aApDLARSYTpqaAqCTBKysAa6sAbSsAbaHA7itAbiHA7z9ArwDvj7C7QHQiQLS8AHSuwHSiQLWuwHcggPgmgLg1wLmmQLypQMcAJb-3iYcFQAALBUAAIUAEhgIMC4wMDMyNDIcFoDA6JaAguT5qgEWlf2n68q43rkEACXKARUKOAgyMTQ1Mzg5NRwAGAo0MjYzNDk1MzQyFAZZFAIAGAE3AA/78edb4e8?m=8&ts=$TS\",\"http://v2.reachmax.cn/click/run.php?a=72BB5A12B30F15E75E68E1B7851F8C50&muid=72BB5A12B30F15E75E68E1B7851F8C50&ver=0&cpid=210470&pub=148&bc=XW8BNox4sH&l=2554900&cid=21453895&plat=2&s=1&rt=1569097107\",\"https://c.w.inmobi.cn/c.asm/HDboppmy1PjBBRbKzAsUWBTIAhWqDBsEWCAoODhjNDg4ZWM5N2I3N2NjMDRmNjUyMTJjNzYyMzdhNDIzOGUzZGI2Mx4gNzJiYjVhMTJiMzBmMTVlNzVlNjhlMWI3ODUxZjhjNTAAIGI5OTkzZDFlMTMwMTIxMWEzZDZmYTQ5MmFmYTNjMDgyBCg2OGVhMDgxZDQzM2M4ODNmYzNiODA3MzExMzE3NDc2OTZhYTM3NzMzHBUEFRgVAhXsBBUCJQAoA2RpcgATARwXz2bV52orPkAX4umVsgxRXkAAFQAYKFkyOXRMbXBqWDJsdWRHVnlMblJ5WVc1emJHRjBhVzl1ZEhKcGNBfn4Wyv33AhwcFoDA6JaAguT5qgEWlf3__9-43v8FAAAVHhcAAAAAAABUQCQAHBIZBQAWyv33AhgDVVNEFryfrcu5Whn1HhS8A4Ibvj6YToJ9rKwBrqwBtKwBuK0BgK8B0rsB1rsBkMsBwu0Biu8B0vAB0IkC0okC5pkCjpoCmpoC4JoC4NcCvP0C3IIDtocDuIcD8qUDoJMEIRhNDAABCwABAAAAIDkwYTkzOThkZWU1OTRlZGRiYzczMzgwMzM5ZDhkNzYzCgACAAAAAAAXOKYABAACQAThR64UeuEKAAQAAAAAAC7_ZQA4IgQAAUBUAAAAAAAABAACQAUAAAAAAAAMAAMIAAEAAAABAAAYbQMAAQAIAAIAAAANBgAFAoAGAAYEcAsABwAAAAUzLjEuMAsACAAAAANBUFAIAAkAAAABAgAKAAgACwAE178EAAxAAMzMzMzMzQgADQAOi3AIAA4AAHF4CwAPAAAAD0RFUklWRURfTEFUX0xPTgAYpAEKAAFGUnlxH3CugwsAAgAAAIwLAAEAAAAcY29tLmpjX2ludGVyLnRyYW5zbGF0aW9udHJpcAMAAgACAAQBAgAFAAYABhAQCwAHAAAABFJUQkQIAA4AAAAACwAPAAAABzMyMFg1NjgGABAAAAsAEQAAACA5MGE5Mzk4ZGVlNTk0ZWRkYmM3MzM4MDMzOWQ4ZDc2MwoAEgAAAAAAqUvGAAYABAAAAAAYmwI2vJ-ty7laqDhBNFlDM3pQejMwZU1zVCtZWSszQUVSR3BiVnlmSmtTVGlpZjJSV3RzU0lWdWt0M3Z1QmZPWlE9PRgGTkFUSVZFHDwcFoDA6JaAguT5qgEWlf3__9-43v8FABaGuoX3o9y80owBIgAAOfUegK8Bgn2CG4rvAY6aApDLARSYTpqaAqCTBKysAa6sAbSsAbaHA7itAbiHA7z9ArwDvj7C7QHQiQLS8AHSuwHSiQLWuwHcggPgmgLg1wLmmQLypQMcAJb-3iYcFQAALBUAAIUAEhgIMC4wMDMyNDIcFoDA6JaAguT5qgEWlf2n68q43rkEACXKARUKOAgyMTQ1Mzg5NRwAGAo0MjYzNDk1MzQyFAZZFAIAGAE3AA/332812f?at=0&am=7&ct=$TS\",\"http://third.mchang.cn/thirdparty/mchang/advertisement/click?p_q_c=eyJNY2FkaWQiOiJkMzllMTk5Ni0xNzQyLTQyOGUtODQ4OC0yMzIxNDIxMGM4M2YiLCJjb25uIjoiMSIsIm1hYyI6IkE4N0QxMkIwMDEwNCIsImFpZCI6IjU2OTBlZTY4NDljZDRkNWIiLCJhZGkiOiJkMzllMTk5Ni0xNzQyLTQyOGUtODQ4OC0yMzIxNDIxMGM4M2YiLCJkYXRlIjoiMjAxOTA5MjIiLCJjaGFubmVsaWQiOiIxMjA1IiwiY2xpY2tpbnRlcmNlcHQiOmZhbHNlLCJ0aW1lIjoxNTY5MDk3MTA3NDExLCJnZ2lkIjoiN2ZhMmQyOTdlYmJjNjA1NzE4M2JjOGQxOTg0OGM5NDAiLCJjdXJIb3VyIjoiNCIsImNhcnJpZXIiOiIxIiwic1BrZ05hbWUiOiIiLCJpbWd1cmwiOiJiOGVjMmFmNTJkM2JiMjdkIiwib3MiOiIwIiwib3N2IjoiOC4xLjAiLCJpbWVpIjoiODYwMjE5MDQwNjEzMTQ4IiwiYWR0eXBlIjoiNCIsImFkc3BhY2VpZCI6IjE1NTM5MTc2NTA5MTAiLCJwa2duYW1lIjoiY24ueXlzaW5nIiwiYXBpdmVyc2lvbiI6IjMuMCIsImlwIjoiMTE1LjIxNy4xMTMuMTczIiwiaW1nVXJsIjoiaHR0cCUzQSUyRiUyRmkubC5pbm1vYmljZG4uY24lMkZhZHRvb2xzJTJGbmF0aXZlYWRzJTJGcHJvZCUyRmltYWdlcyUyRjY1MjM5MjQ4Mzc5NTAwNDkxM2EwZWRjNGItYzE5MS00YTQ0LWE0OTAtOGFlOGIxY2NiODE5LmpwZyIsImFwcG5hbWUiOiIlRTklQkElQTYlRTUlOTQlQjEiLCJkZXZpY2UiOiJIVUFXRUklMkJQQVItQUwwMCIsInVhIjoiTW96aWxsYSUyRjUuMCslMjhMaW51eCUzQitBbmRyb2lkKzguMS4wJTNCK1BBUi1BTDAwK0J1aWxkJTJGSFVBV0VJUEFSLUFMMDAlM0Ird3YlMjkrQXBwbGVXZWJLaXQlMkY1MzcuMzYrJTI4S0hUTUwlMkMrbGlrZStHZWNrbyUyOStWZXJzaW9uJTJGNC4wK0Nocm9tZSUyRjQzLjAuMjM1Ny42NStNb2JpbGUrU2FmYXJpJTJGNTM3LjM2IiwiY2xpY2tVcmwiOiJodHRwJTNBJTJGJTJGZS5jbi5taWFvemhlbi5jb20lMkZyJTJGayUzRDIxMzQ1NjglMjZwJTNEN1JMTlolMjZkeCUzRF9fSVBEWF9fJTI2cnQlM0QyJTI2bnMlM0QxMTUuMjE3LjExMy4xNzMlMjZuaSUzRF9fSUVTSURfXyUyNnYlM0RfX0xPQ19fJTI2eGElM0RfX0FEUExBVEZPUk1fXyUyNnRyJTNEX19SRVFVRVNUSURfXyUyNm1vJTNEMCUyNm0wJTNEJTI2bTBhJTNEJTI2bTElM0RfX0FORFJPSURJRDFfXyUyNm0xYSUzRCUyNm0yJTNENzJiYjVhMTJiMzBmMTVlNzVlNjhlMWI3ODUxZjhjNTAlMjZtNCUzRF9fQUFJRF9fJTI2bTUlM0QlMjZtNiUzRCUyNm02YSUzRF9fTUFDX18lMjZ2byUzRDMyMmUwMzljYiUyNnZyJTNEMiUyNm8lM0RodHRwcyUyNTNBJTI1MkYlMjUyRmd4Yi5tbXN0YXQuY29tJTI1MkZneGIuZ2lmJTI1M0Z0JTI1M0RodHRwcyUyNTI1M0ElMjUyNTJGJTI1MjUyRmVxdWl0eS12aXAudG1hbGwuY29tJTI1MjUyRmFnZW50JTI1MjUyRm1vYmlsZS5odG0lMjUyNTNGYWdlbnRJZCUyNTI1M0QzMDY2MjYlMjUyNTI2X2JpbmQlMjUyNTNEdHJ1ZSUyNTI1MjZtbV91bmlkJTI1MjUzRDFfMjA0MzM1Ml81NjA1MDEwMTVlNmQ1ZjU2MDcwMjA1M2EwMDA1NTkzOTA0MGE1MDA0MDElMjUyNnYlMjUzRGE3YWRiMGMwY2FkYSUyNTI2ZGklMjUzRCUyNTI2ZGltJTI1M0Q3MmJiNWExMmIzMGYxNWU3NWU2OGUxYjc4NTFmOGM1MCIsImltc2kiOiI0NjAwNzcxNTExMDE0NTYiLCJpc0dldEFkRHBsaW5rIjpmYWxzZX0=&adid=7321a6dfb5f1fcddaab1b59c91b8e4d4&xpclick=00&r_click_d_x=-999&r_click_d_y=-999&r_click_u_x=-999&r_click_u_y=-999&a_click_d_x=-999&a_click_d_y=-999&a_click_u_x=-999&a_click_u_y=-999\",\"http://139.199.102.235:16600/106001?enc=af8f6f267a207af0a758a00742921ce98050e336300ac5210734a3fb35f64532271c13ffef23e8100232cfde143b822c26cd7eb9a15bbb665efbaf879475c5b770ab555d46adcfe119c2a0cfe16a1ede25ed1785a950fb80b41991175e9395c6fbb6f36b94feca7bf101097d43ed9bea045a189cdcf7e3c9ac22bb6586c1a941e1a3dfc2cd1e8712f6715a21cb66f8e6cabe5bf3625d2587b2395b14f43ff62a73db4b5e184795f29a4d9cb17ce7401510926814881b85df5f4eaedb9c4bd9deceb524c0d2bd93a7a80eef0ee676243da44f23edbb9c65a3cd231813e869a0198383d456496f602d30f8f9671510b004849c8927f41f1ac65de5a362a228ef7fc59b29f6dcbbea4091e1b303e827c18e08178dd899515b9f88f6b0b424f14488743f63e9dee96357edee7cd13c0f6838cf1104f6779afb2988f6b0b424f144881b6bff96d7de89ea1e71f50e492dd840542f4748b26d55d3331d02ffc7b9c0e55731128f0671c4f77f1e72eaf99c88e8c304441862171c1fb5f8fada040e6ed461cdcbe72afdf2c9480e2c66a50910c99aa0a6e47edd22140043e5083d40f21d8b262a3b968fc0f041257577e81d1c1f90c5925b2c9f0a1309d7cbc86f7097cf20883d563f6d86809d0c38924a4c2a47173339362b470a55576b636f0f9581bd1598d3f341d1ffc6704fee47551d25b2dd566546680492eddb37b045c31d4bcc&s=%7B%22down_x%22%3A%22cb_down_x%22%2C%22down_y%22%3A%22cb_down_y%22%2C%22up_x%22%3A%22cb_up_x%22%2C%22up_y%22%3A%22cb_up_y%22%7D\"],\"admt\":1,\"adi\":\"http://i.l.inmobicdn.cn/adtools/nativeads/prod/images/652392483795004913a0edc4b-c191-4a44-a490-8ae8b1ccb819.jpg\",\"adct\":1,\"imptrackers\":[\"http://et.w.inmobi.cn/c.asm/HDboppmy1PjBBRbKzAsUWBTIAhWqDBsEWCAoODhjNDg4ZWM5N2I3N2NjMDRmNjUyMTJjNzYyMzdhNDIzOGUzZGI2Mx4gNzJiYjVhMTJiMzBmMTVlNzVlNjhlMWI3ODUxZjhjNTAAIGI5OTkzZDFlMTMwMTIxMWEzZDZmYTQ5MmFmYTNjMDgyBCg2OGVhMDgxZDQzM2M4ODNmYzNiODA3MzExMzE3NDc2OTZhYTM3NzMzHBUEFRgVAhXsBBUCJQAoA2RpcgATABwXz2bV52orPkAX4umVsgxRXkAAFQAYKFkyOXRMbXBqWDJsdWRHVnlMblJ5WVc1emJHRjBhVzl1ZEhKcGNBfn4Wyv33AhwcFoDA6JaAguT5qgEWlf3__9-43v8FAAAVHhcAAAAAAABUQCQAHBIZBQAWyv33AhgDVVNEFryfrcu5Whn1HhS8A4Ibvj6YToJ9rKwBrqwBtKwBuK0BgK8B0rsB1rsBkMsBwu0Biu8B0vAB0IkC0okC5pkCjpoCmpoC4JoC4NcCvP0C3IIDtocDuIcD8qUDoJMEIRhNDAABCwABAAAAIDkwYTkzOThkZWU1OTRlZGRiYzczMzgwMzM5ZDhkNzYzCgACAAAAAAAXOKYABAACQAThR64UeuEKAAQAAAAAAC7_ZQA4IgQAAUBUAAAAAAAABAACQAUAAAAAAAAMAAMIAAEAAAABAAAYbQMAAQAIAAIAAAANBgAFAoAGAAYEcAsABwAAAAUzLjEuMAsACAAAAANBUFAIAAkAAAABAgAKAAgACwAE178EAAxAAMzMzMzMzQgADQAOi3AIAA4AAHF4CwAPAAAAD0RFUklWRURfTEFUX0xPTgAYpAEKAAFGUnlxH3CugwsAAgAAAIwLAAEAAAAcY29tLmpjX2ludGVyLnRyYW5zbGF0aW9udHJpcAMAAgACAAQBAgAFAAYABhAQCwAHAAAABFJUQkQIAA4AAAAACwAPAAAABzMyMFg1NjgGABAAAAsAEQAAACA5MGE5Mzk4ZGVlNTk0ZWRkYmM3MzM4MDMzOWQ4ZDc2MwoAEgAAAAAAqUvGAAYABAAAAAAYmwI2vJ-ty7laqDhBNFlDM3pQejMwZU1zVCtZWSszQUVSR3BiVnlmSmtTVGlpZjJSV3RzU0lWdWt0M3Z1QmZPWlE9PRgGTkFUSVZFHDwcFoDA6JaAguT5qgEWlf3__9-43v8FABaGuoX3o9y80owBIgAAOfUegK8Bgn2CG4rvAY6aApDLARSYTpqaAqCTBKysAa6sAbSsAbaHA7itAbiHA7z9ArwDvj7C7QHQiQLS8AHSuwHSiQLWuwHcggPgmgLg1wLmmQLypQMcAJb-3iYcFQAALBUAAIUAEhgIMC4wMDMyNDIcFoDA6JaAguT5qgEWlf2n68q43rkEACXKARUKOAgyMTQ1Mzg5NRwAGAo0MjYzNDk1MzQyFAZZFAIAGAE3AA/78edb4e8?m=18&ts=$TS\",\"http://g.cn.miaozhen.com/x/k=2134568&p=7RLNZ&dx=__IPDX__&rt=2&ns=115.217.113.173&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&tr=__REQUESTID__&mo=0&m0=&m0a=&m1=__ANDROIDID1__&m1a=&m2=72bb5a12b30f15e75e68e1b7851f8c50&m4=__AAID__&m5=&m6=&m6a=__MAC__&o=\",\"http://v2.reachmax.cn/count/run.php?a=72BB5A12B30F15E75E68E1B7851F8C50&muid=72BB5A12B30F15E75E68E1B7851F8C50&ver=0&cpid=210470&pub=148&bc=XW8BNox4sH&l=2554900&cid=21453895&plat=2&s=1&rt=1569097107&t=s&ssl=0\",\"http://third.mchang.cn/thirdparty/mchang/advertisement/show?p_q_s=eyJNY2FkaWQiOiJkMzllMTk5Ni0xNzQyLTQyOGUtODQ4OC0yMzIxNDIxMGM4M2YiLCJvcyI6IjAiLCJjb25uIjoiMSIsIm9zdiI6IjguMS4wIiwiaW1laSI6Ijg2MDIxOTA0MDYxMzE0OCIsImFkdHlwZSI6IjQiLCJtYWMiOiJBODdEMTJCMDAxMDQiLCJhZHNwYWNlaWQiOiIxNTUzOTE3NjUwOTEwIiwiYWRpIjoiZDM5ZTE5OTYtMTc0Mi00MjhlLTg0ODgtMjMyMTQyMTBjODNmIiwiYWlkIjoiNTY5MGVlNjg0OWNkNGQ1YiIsImRhdGUiOiIyMDE5MDkyMiIsInBrZ25hbWUiOiJjbi55eXNpbmciLCJhcGl2ZXJzaW9uIjoiMy4wIiwiaXAiOiIxMTUuMjE3LjExMy4xNzMiLCJjaGFubmVsaWQiOiIxMjA1IiwiaW1nVXJsIjoiaHR0cCUzQSUyRiUyRmkubC5pbm1vYmljZG4uY24lMkZhZHRvb2xzJTJGbmF0aXZlYWRzJTJGcHJvZCUyRmltYWdlcyUyRjY1MjM5MjQ4Mzc5NTAwNDkxM2EwZWRjNGItYzE5MS00YTQ0LWE0OTAtOGFlOGIxY2NiODE5LmpwZyIsInRpbWUiOjE1NjkwOTcxMDc0MTEsImdnaWQiOiI3ZmEyZDI5N2ViYmM2MDU3MTgzYmM4ZDE5ODQ4Yzk0MCIsImN1ckhvdXIiOiI0IiwiYXBwbmFtZSI6IiVFOSVCQSVBNiVFNSU5NCVCMSIsImRldmljZSI6IkhVQVdFSSUyQlBBUi1BTDAwIiwidWEiOiJNb3ppbGxhJTJGNS4wKyUyOExpbnV4JTNCK0FuZHJvaWQrOC4xLjAlM0IrUEFSLUFMMDArQnVpbGQlMkZIVUFXRUlQQVItQUwwMCUzQit3diUyOStBcHBsZVdlYktpdCUyRjUzNy4zNislMjhLSFRNTCUyQytsaWtlK0dlY2tvJTI5K1ZlcnNpb24lMkY0LjArQ2hyb21lJTJGNDMuMC4yMzU3LjY1K01vYmlsZStTYWZhcmklMkY1MzcuMzYiLCJjYXJyaWVyIjoiMSIsImltc2kiOiI0NjAwNzcxNTExMDE0NTYiLCJpbWd1cmwiOiJiOGVjMmFmNTJkM2JiMjdkIn0=&adid=2ac42bec99c58310dedff561348c624f&xpshow=00\",\"http://139.199.102.235:16600/106001?enc=af8f6f267a207af0a758a00742921ce98050e336300ac5210734a3fb35f64532271c13ffef23e8100232cfde143b822c26cd7eb9a15bbb665efbaf879475c5b770ab555d46adcfe119c2a0cfe16a1ede25ed1785a950fb80b41991175e9395c6fbb6f36b94feca7bf101097d43ed9bea045a189cdcf7e3c9ac22bb6586c1a941e1a3dfc2cd1e8712f6715a21cb66f8e6cabe5bf3625d2587b2395b14f43ff62a73db4b5e184795f29a4d9cb17ce7401510926814881b85df5f4eaedb9c4bd9deceb524c0d2bd93a7a80eef0ee676243da44f23edbb9c65a3cd231813e869a0198383d456496f602d30f8f9671510b004849c8927f41f1ac65de5a362a228ef7fc59b29f6dcbbea4091e1b303e827c18e08178dd899515b9f88f6b0b424f14488743f63e9dee96357edee7cd13c0f6838cf1104f6779afb2988f6b0b424f144881b6bff96d7de89ea1e71f50e492dd840542f4748b26d55d3331d02ffc7b9c0e55731128f0671c4f77f1e72eaf99c88e8c304441862171c1fb5f8fada040e6ed461cdcbe72afdf2c9480e2c66a50910c99aa0a6e47edd22140043e5083d40f21d8b262a3b968fc0f041257577e81d1c1f90c5925b2c9f0a1309d7cbc86f7097cf20883d563f6d86809d0c38924a4c2a47173339362b470a55576b636f0f9581bd1598d3f341d1ffc6704fee47551d25b2dd566546680492edbf42e6111a6b4af8\"],\"targeturl\":\"http://e.cn.miaozhen.com/r/k=2134568&p=7RLNZ&dx=__IPDX__&rt=2&ns=115.217.113.173&ni=__IESID__&v=__LOC__&xa=__ADPLATFORM__&tr=__REQUESTID__&mo=0&m0=&m0a=&m1=__ANDROIDID1__&m1a=&m2=72bb5a12b30f15e75e68e1b7851f8c50&m4=__AAID__&m5=&m6=&m6a=__MAC__&vo=322e039cb&vr=2&o=https%3A%2F%2Fgxb.mmstat.com%2Fgxb.gif%3Ft%3Dhttps%253A%252F%252Fequity-vip.tmall.com%252Fagent%252Fmobile.htm%253FagentId%253D306626%2526_bind%253Dtrue%2526mm_unid%253D1_2043352_560501015e6d5f560702053a00055939040a500401%26v%3Da7adb0c0cada%26di%3D%26dim%3D72bb5a12b30f15e75e68e1b7851f8c50\"},\"adid\":\"10001\",\"price\":0.0,\"w\":0,\"h\":0,\"id\":\"1d7f3bb8-9e6e-439c-93b2-425bc2f508fb\",\"impid\":\"1\",\"cid\":\"A117401000002\"}],\"group\":0}],\"nbr\":200}";
                bidResponse = JSON.parseObject(json, BidResponse.class);
                bidResponse.setId(bidRequest.getId());
            }

            // 获取广告成功
            if (null != bidResponse && bidResponse.getNbr() == 200) {
                //map = BeanMap.create(bidResponse);
                //响应实例
                JSONObject map = new JSONObject();
                map.put("id", bidResponse.getId());
                map.put("nbr", bidResponse.getNbr());
                if (!StringUtils.isEmpty(bidResponse.getCur())) {
                    map.put("cur", bidResponse.getCur());
                }
                if (!StringUtils.isEmpty(bidResponse.getBidid())) {
                    map.put("bidid", bidResponse.getBidid());
                }
                if (!StringUtils.isEmpty(bidResponse.getCustomdata())) {
                    map.put("customdata", bidResponse.getCustomdata());
                }
                JSONArray seatbids = new JSONArray();
                for (BidResponse.SeatBid seatBidItem : bidResponse.getSeatbid()) {
                    JSONObject seatBid = new JSONObject();
                    seatBid.put("group", seatBidItem.getGroup());
                    if (!StringUtils.isEmpty(seatBidItem.getSeat())) {
                        seatBid.put("seat", seatBidItem.getSeat());
                    }
                    JSONArray bids = new JSONArray();
                    for (BidResponse.Bid bidItem : seatBidItem.getBid()) {
                        JSONObject bid = new JSONObject();
                        if (!StringUtils.isEmpty(bidItem.getId())) {
                            bid.put("id", bidItem.getId());
                        }
                        if (!StringUtils.isEmpty(bidItem.getImpid())) {
                            bid.put("impid", bidItem.getImpid());
                        }
                        bid.put("price", bidItem.getPrice());
                        if (!StringUtils.isEmpty(bidItem.getNurl())) {
                            bid.put("nurl", bidItem.getNurl());
                        }
                        if (!StringUtils.isEmpty(bidItem.getBurl())) {
                            bid.put("burl", bidItem.getBurl());
                        }
                        if (!StringUtils.isEmpty(bidItem.getLurl())) {
                            bid.put("lurl", bidItem.getLurl());
                        }
                        if (!StringUtils.isEmpty(bidItem.getAdm())) {
                            bid.put("adm", bidItem.getAdm());
                        }
                        if (!StringUtils.isEmpty(bidItem.getAdid())) {
                            bid.put("adid", bidItem.getAdid());
                        }
                        if (null != bidItem.getAdomain() && 0 != bidItem.getAdomain().length) {
                            bid.put("adomain", bidItem.getAdomain());
                        }
                        if (!StringUtils.isEmpty(bidItem.getBundle())) {
                            bid.put("bundle", bidItem.getBundle());
                        }
                        if (!StringUtils.isEmpty(bidItem.getIurl())) {
                            bid.put("iurl", bidItem.getIurl());
                        }
                        if (!StringUtils.isEmpty(bidItem.getCid())) {
                            bid.put("cid", bidItem.getCid());
                        }
                        if (!StringUtils.isEmpty(bidItem.getCrid())) {
                            bid.put("crid", bidItem.getCrid());
                        }
                        if (!StringUtils.isEmpty(bidItem.getTactic())) {
                            bid.put("tactic", bidItem.getTactic());
                        }
                        if (null != bidItem.getCat() && 0 != bidItem.getCat().length) {
                            bid.put("cat", bidItem.getCat());
                        }
                        if (null != bidItem.getAttr() && 0 != bidItem.getAttr().length) {
                            bid.put("attr", bidItem.getAttr());
                        }
                        if (!StringUtils.isEmpty(bidItem.getApi())) {
                            bid.put("api", bidItem.getApi());
                        }
                        if (!StringUtils.isEmpty(bidItem.getProtocol())) {
                            bid.put("protocol", bidItem.getProtocol());
                        }
                        if (!StringUtils.isEmpty(bidItem.getQagmediarating())) {
                            bid.put("qagmediarating", bidItem.getQagmediarating());
                        }
                        if (!StringUtils.isEmpty(bidItem.getLanguage())) {
                            bid.put("language", bidItem.getLanguage());
                        }
                        if (!StringUtils.isEmpty(bidItem.getDealid())) {
                            bid.put("dealid", bidItem.getDealid());
                        }
                        if (!StringUtils.isEmpty(bidItem.getW())) {
                            bid.put("w", bidItem.getW());
                        }
                        if (!StringUtils.isEmpty(bidItem.getH())) {
                            bid.put("h", bidItem.getH());
                        }
                        if (!StringUtils.isEmpty(bidItem.getWratio())) {
                            bid.put("wratio", bidItem.getWratio());
                        }
                        if (!StringUtils.isEmpty(bidItem.getHratio())) {
                            bid.put("hratio", bidItem.getHratio());
                        }
                        if (!StringUtils.isEmpty(bidItem.getExp())) {
                            bid.put("exp", bidItem.getExp());
                        }
                        if (null != bidItem.getExt()) {
                            JSONObject bidExt = new JSONObject();
                            bidExt.put("admt", bidItem.getExt().getAdmt());
                            bidExt.put("adct", bidItem.getExt().getAdct());
                            if (!StringUtils.isEmpty(bidItem.getExt().getIcon())) {
                                bidExt.put("icon", bidItem.getExt().getIcon());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getAdi())) {
                                bidExt.put("adi", bidItem.getExt().getAdi());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getAdt())) {
                                bidExt.put("adt", bidItem.getExt().getAdt());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getAds())) {
                                bidExt.put("ads", bidItem.getExt().getAds());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getDan())) {
                                bidExt.put("dan", bidItem.getExt().getDan());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getTargeturl())) {
                                bidExt.put("targeturl", bidItem.getExt().getTargeturl());
                            }
                            if (!StringUtils.isEmpty(bidItem.getExt().getDeeplinkurl())) {
                                bidExt.put("deeplinkurl", bidItem.getExt().getDeeplinkurl());
                            }
                            if (null != bidItem.getExt().getImptrackers() && 0 != bidItem.getExt().getImptrackers().length) {
                                bidExt.put("imptrackers", bidItem.getExt().getImptrackers());
                            }
                            if (null != bidItem.getExt().getClicktrackers() && 0 != bidItem.getExt().getClicktrackers().length) {
                                bidExt.put("clicktrackers", bidItem.getExt().getClicktrackers());
                            }
                            if (null != bidItem.getExt().getDownloadbegintrackers() && 0 != bidItem.getExt().getDownloadbegintrackers().length) {
                                bidExt.put("downloadbegintrackers", bidItem.getExt().getDownloadbegintrackers());
                            }
                            if (null != bidItem.getExt().getDownloadendtrackers() && 0 != bidItem.getExt().getDownloadendtrackers().length) {
                                bidExt.put("downloadendtrackers", bidItem.getExt().getDownloadendtrackers());
                            }
                            if (null != bidItem.getExt().getInstalledtrackers() && 0 != bidItem.getExt().getInstalledtrackers().length) {
                                bidExt.put("installedtrackers", bidItem.getExt().getInstalledtrackers());
                            }
                            if (null != bidItem.getExt().getDeeplinktrackers() && 0 != bidItem.getExt().getDeeplinktrackers().length) {
                                bidExt.put("deeplinktrackers", bidItem.getExt().getDeeplinktrackers());
                            }
                            bid.put("ext", bidExt);
                        }
                        bids.add(bid);
                    }
                    seatBid.put("bid", bids);
                    seatbids.add(seatBid);
                }
                map.put("seatbid", seatbids);
                long endTime = System.currentTimeMillis();
                long usedTime = endTime - startTime;
                if (i > 0) {
                    log.debug("token:{}，耗时：{}，广告：{}", token, usedTime, JSON.toJSONString(map));
                } else {
                    log.trace("token:{}，耗时：{}，广告：{}", token, usedTime, JSON.toJSONString(map));
                }
                return JSON.toJSONString(map);
            } else {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return "";
        }
    }
}
