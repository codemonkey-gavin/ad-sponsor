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
        WebResponseResult responseResult = WebUtil.HttpRequestPost("http://online.quickh5.com/api/public/bid?token=282196822743484c8e95bda5b1a19905",
                json, 500);
        System.out.println(responseResult.getStatusCode());
        System.out.println(responseResult.getResponse());
    }

    @Test
    public void webRequestPostZulu() {
        String json = "{\"id\":\"31934.002-67.hfnr2.295329763.py4j1z.c35f.620056.妈妈宝典-Android-Banner\",\"imp\":[{\"id\":\"47ce17aa840143049c2f9607604949aa\",\"instl\":0,\"bidfloor\":0.01,\"bidfloorcur\":\"CNY\",\"banner\":{\"w\":640,\"h\":100,\"pos\":0}}],\"app\":{\"id\":\"EzqIze\",\"name\":\"妈妈宝典\",\"bundle\":\"cn.mmbd.app\"},\"device\":{\"ua\":\"Mozilla/5.0 (Linux; Android 4.4.4; OPPO R7s Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36\",\"geo\":{\"lat\":45.6864853,\"lon\":45.6864853},\"ip\":\"222.217.214.6\",\"devicetype\":1,\"connectiontype\":2,\"make\":\"OPPO\",\"model\":\"OPPO R7s\",\"os\":\"android\",\"osv\":\"4.4.4\",\"w\":1080,\"h\":2030,\"ppi\":0,\"pxratio\":0.0,\"carrier\":\"中国移动\",\"mccmnc\":\"46000\",\"didsha1\":\"5ece78c04f2bac4e62e0e673b5af166f72b8f2ad\",\"didmd5\":\"adfa611fe5b69c8646b4087ba194e589\",\"dpidsha1\":\"b066d6056d1b38c75146805a21fb14827c31caa6\",\"dpidmd5\":\"53f8de9a3b180c7f3e3f837d3f9d583b\",\"macsha1\":\"090a76ec339bf0f765df35a96a33a1935e208867\",\"macmd5\":\"fc899c63873044072888bdf9ad3f988b\",\"ext\":{\"androidid\":\"ffd96124846edad2\",\"imei\":\"869411025532499\",\"mac\":\"2C:5B:B8:C8:0C:5F\"}}}";
        for (int i = 0; i < 1; i++) {
            WebResponseResult responseResult = WebUtil.HttpRequestPost("http://39.106.12.91/api/bid/104605@10001",
                    json,
                    500);
//            System.out.println(responseResult.getStatusCode());
//            System.out.println(responseResult.getResponse());
            if (responseResult.getStatusCode() == 200) {
                System.out.println(responseResult.getResponse());
            }
        }
    }
}
