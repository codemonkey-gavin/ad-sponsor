package com.adexchange.adsponsor;

import com.adexchange.adsponsor.advertiser.Adview;
import com.adexchange.adsponsor.dto.WebResponseResult;
import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.entity.CreativeMaterial;
import com.adexchange.adsponsor.util.WebUtil;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdSponsorApplicationTests {
    @Test
    public void testRequestAdview() {
        String json = "{\"id\":\"31934.002-67.hfnr2.295329763.py4j1z.c35f.620056.妈妈宝典-Android-Banner\",\"imp\":[{\"id\":\"47ce17aa840143049c2f9607604949aa\",\"instl\":0,\"bidfloor\":0.01,\"bidfloorcur\":\"CNY\",\"banner\":{\"w\":640,\"h\":100,\"pos\":0}}],\"app\":{\"id\":\"EzqIze\",\"name\":\"妈妈宝典\",\"bundle\":\"cn.mmbd.app\"},\"device\":{\"ua\":\"Mozilla/5.0 (Linux; Android 4.4.4; OPPO R7s Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36\",\"geo\":{\"lat\":45.6864853,\"lon\":45.6864853},\"ip\":\"222.217.214.6\",\"devicetype\":1,\"connectiontype\":2,\"make\":\"OPPO\",\"model\":\"OPPO R7s\",\"os\":\"android\",\"osv\":\"4.4.4\",\"w\":1080,\"h\":2030,\"ppi\":0,\"pxratio\":0.0,\"carrier\":\"中国移动\",\"mccmnc\":\"46000\",\"didsha1\":\"5ece78c04f2bac4e62e0e673b5af166f72b8f2ad\",\"didmd5\":\"adfa611fe5b69c8646b4087ba194e589\",\"dpidsha1\":\"b066d6056d1b38c75146805a21fb14827c31caa6\",\"dpidmd5\":\"53f8de9a3b180c7f3e3f837d3f9d583b\",\"macsha1\":\"090a76ec339bf0f765df35a96a33a1935e208867\",\"macmd5\":\"fc899c63873044072888bdf9ad3f988b\",\"ext\":{\"androidid\":\"ffd96124846edad2\",\"imei\":\"869411025532499\",\"mac\":\"2C:5B:B8:C8:0C:5F\"}}}";
        BidRequest request = JSON.parseObject(json, BidRequest.class);
        Adview adview = new Adview();
        BidResponse response = adview.getAds(request, "", 300);
        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testUploadCreative() {
        CreativeMaterial creativeMaterial = new CreativeMaterial();
        CreativeMaterial.Creative creative = new CreativeMaterial.Creative();
        creative.setAdvertiserid("114604");
        creative.setCreativeid("802103");
        creative.setName("test");
        creative.setLandingpage("http://161.117.83.145:8010/ViewIndex.html");
        creative.setType(1);
        CreativeMaterial.CreativeBanner banner = new CreativeMaterial.CreativeBanner();
        CreativeMaterial.MaterialImage icon = new CreativeMaterial.MaterialImage();
        icon.setWidth(50);
        icon.setHeight(50);
        icon.setUrl("http://161.117.83.145:8010/online/50x50.jpeg");
        banner.setIcon(icon);

        CreativeMaterial.MaterialImage image = new CreativeMaterial.MaterialImage();
        image.setWidth(960);
        image.setHeight(640);
        image.setUrl("http://161.117.83.145:8010/online/960x640.jpeg");
        banner.setMaterials(new CreativeMaterial.MaterialImage[]{image});
        creative.setBanner(banner);
        creativeMaterial.setCreative(new CreativeMaterial.Creative[]{creative});

        String json = JSON.toJSONString(creativeMaterial);
        System.out.println(json);
    }
}
