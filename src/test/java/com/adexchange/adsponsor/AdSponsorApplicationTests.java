package com.adexchange.adsponsor;

import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.util.WebUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdSponsorApplicationTests {

    @Test
    public void webRequestGet() {
        WebResponseResult responseResult = WebUtil.HttpRequestGet("http://server.adview" +
                ".com/api/public/campaigns?token=a8566998-b694-4bb6-b34c-cd52fd25925b", 200);
    }

    @Test
    public void webRequestPost() {
        String json = "{\"id\":\"32024.001-01.yt8c34.44824633.pxpzub.41a5.620056.章鱼直播-iOS-开屏\",\"imp\":[{\"id\":\"c47aaadaf299429d2261568294579714\",\"banner\":{\"w\":640,\"h\":960,\"mimes\":[\"image/jpg\",\"image/jpeg\",\"image/png\",\"image/gif\"]},\"instl\":2,\"tagid\":\"32024\",\"bidfloor\":0.01,\"bidfloorcur\":\"CNY\",\"clickbrowser\":0,\"secure\":0}],\"site\":null,\"app\":{\"id\":\"\",\"name\":\"章鱼直播\",\"bundle\":\"com.zhangyu.zhangyutv\",\"domain\":\"\",\"storeurl\":\"\"},\"device\":{\"ua\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57\",\"ip\":\"125.118.244.48\",\"devicetype\":1,\"make\":\"apple\",\"model\":\"iPhone7,1\",\"os\":\"ios\",\"osv\":\"12.1.4\",\"h\":1920,\"w\":1080,\"ppi\":0,\"js\":0,\"carrier\":\"China Mobile\",\"mccmnc\":\"46000\",\"connectiontype\":2,\"ifa\":\"488A051C-9921-4C27-88AA-29C27589FBC9\",\"didsha1\":\"\",\"didmd5\":\"\",\"dpidsha1\":\"\",\"dpidmd5\":\"\",\"macsha1\":\"\",\"macmd5\":\"\"},\"user\":null,\"test\":0,\"at\":2,\"Tmax\":200,\"cur\":[\"CNY\"]}";
        for (int i = 1; i < 100; i++) {
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://online.quickh5.com/api/public/bid?token=282196822743484c8e95bda5b1a19905",
                    json,
                    500);
            System.out.println(responseResult.getStatusCode());
            System.out.println(responseResult.getResponse());
        }
    }
    @Test
    public void webRequestPostGzip() {
        String json = "{\"n\":1,\"apv\":\"1.0.6\",\"bid\":\"20190911-212812_bidreq_135-3296-am0A-1784\",\"aid\":\"A117403000001\",\"adt\":3,\"adsw\":768,\"adsh\":1024,\"cootype\":2,\"bidprice\":5.0,\"ti\":{\"bn\":\"OPPO\",\"hm\":\"OPPO\",\"ht\":\"OPPOR9s\",\"os\":0,\"ov\":\"6.0.1\",\"sw\":1080,\"sh\":1920,\"ch\":\"p1174@tl\",\"ei\":\"863388038553132\",\"mac\":\"6C:5C:14:39:B6:10\",\"andid\":\"ca6e0981d8366151\",\"idfa\":null,\"oid\":\"\",\"ip\":\"113.83.49.254\",\"ua\":\"Mozilla/5.0 (Linux; Android 6.0.1; OPPO R9s Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36\",\"dpi\":0.0,\"smd\":0,\"lon\":0.0,\"lat\":0.0,\"pkg\":\"com.ldzs.zhangxin\",\"apnm\":\"%e8%9a%82%e8%9a%81%e7%9c%8b%e7%82%b9\",\"dplink\":1,\"si\":\"46000\",\"nt\":3}}";
        WebResponseResult responseResult = WebUtil.HttpRequestPost("http://testapi.i-changemaker.com:9999/105003",
                json,
                500);
        System.out.println(responseResult.getStatusCode());
        System.out.println(responseResult.getResponse());
    }
}
