package com.adexchange.adsponsor.controller;

import com.adexchange.adsponsor.entity.BidRequest;
import com.adexchange.adsponsor.entity.BidResponse;
import com.adexchange.adsponsor.service.AdDispatcherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @RequestMapping(value = "/bid/{token}", method = RequestMethod.POST)
    public Map<String, Object> openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
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

            BidResponse bidResponse = null;
            Integer i = token.indexOf("@");
            if (i > 0) {
                String[] array = token.split("@");
                String advertiserId = array[1];
                bidResponse = adDispatcherService.getAds(bidRequest, advertiserId);
            } else {
                log.debug(JSON.toJSONString(bidRequest));
                String json = "{\"id\":\"1\",\"cur\":\"CNY\",\"seatbid\":[{\"bid\":[{\"ext\":{\"deeplinktrackers" +
                        "\":[\"http://adx.cloud-cube.net/adx/dp_track?bidid=15689729089370060292273123&sadid=0&tagid=930" +
                        "&appid=583&did=31\",\"http://data.wedooapp" +
                        ".com:6699/106001?enc" +
                        "=f2b743535d5cd195d26cb4e5d4b587cb7af29a3b9d0fb1fac2024e7ff32ac8614d99a215294b1868f5641e921e5d5da312c0bdd4839723ee5d09143412b8d554a69b24b3cd5fa08517ad4862ecdaef5c0e74cbf45ed2a9bb7ccefe6b0723dbcdf76910cfbc6ff8889a86365013020fd52767e88f2f058109fe09ffbcc0e312f714ac1f7edfb66370720e9d1ed109796046228c862106295a9971f012f73cfcfdcdde008f72828842360a099119af8982a4e97187e681bad9ab76652a63be6c5b03772123440f495541d0fc2a5f5c6e45db16101f6b1195cecd3134790e66dfedcb0fc59338a6978ce8cd25786f0ec4ceaeb1738b3baa07fab5571ed2db8ed1928a5fb23cbc89965851db7a28a61b34b88680efa97ca058e7f239efdebb65b84768b7057c2731150d51db7a28a61b34b8e735c06f6d0b8246ae132712ba9af02552fbc2408629ff703d0285c5a2127bd2aacb47137167b845cfe06856cc10405945e14f0dead2f91593a0328a567ef3f8c43fb839ffdfaa4031da97447286ed3e5b2ff87aeea91a4341590f0af2765ad8ba57ff74c4764fb64418ddce10178bde802977938e1737435faf049ef419c3a02e83922653eec0e43e1ded422dda8949cf16b0eb8631ad13ca76e01f16dd42d6ddcede786948b58e\"],\"clicktrackers\":[\"http://uat1.bfsspadserver.8le8le.com/adClick?v=4&b=false&st=900000&p=&g=https%3A%2F%2Fccc-x.jd.com%2Fdsp%2Fnc%3Fext%3DaHR0cHM6Ly9zaG9wLm0uamQuY29tLz9zaG9wSWQ9NzkyMjM5JlBUQUc9MTcwNTEuOS4xJmFkX29kPTE%26log%3DMgtoe8BUAyMbZDGYXsVetJGsKFEHzsIL6kUA39e0Pn29DMVkiGXK5aXvA7xbDDgiEVvkaWBPrhJiBFYTdNVtL88aRM3nL7DYjAebGVWmT9LcDJ2lqIKY3YBFhGYUp30kYQd8sYtUlSdZyKou69z6PfRlUdIJ-MUpvNMWOtRgLzQ-4d4qhYnK-xst4WHEeolWFM2IV_aaYm_9ss2jrz-1ORJOdMNF5wiS7nA5qdn8f3DWXFuN7nMZD22570z2SGhNUSbWWhJFYNZjxxt3dnZsD4igNJok9Obqzk4BJhY9PXGRneqmmRo3KTX4E4CdzI-NJeAyvI6bbH_Rk39xUmb2axabpNDyonmg3G7WkCvplnHAzo_ajMUEuGz0puJT6dORR8yXZAY55oLGc2wMdgtGoyiDAkK5C8v0Djl4aPeXjuQQh2KGYlLHxbvyyuEitBcrFYEQwm5jQL5cNSiNdTY3FPyEIF7j--eos4_4ukcWZ7xNsgjeFxkBdMNMejQCxQgaKCCV2UlpBrW1fYR3NiqSYnL-bc9odp1-TmAccuco05MHhvueLSMfew5X58625nbw%26v%3D404&i=15689729089370060292273123&r=1001879&d=68928&req=22484853441077249&resp=224848534410772491&u=99000877591185&s=800144&ty=0&tr=1002258&tm=800166&dp=01&drid=&deal=&t=1568972908939\",\"http://adx.cloud-cube.net/adx/AdxPvClick?bidid=15689729089370060292273123&type=2&tagid=930&uid=31\",\"http://data.wedooapp.com:6699/106001?enc=f2b743535d5cd195d26cb4e5d4b587cb7af29a3b9d0fb1fac2024e7ff32ac8614d99a215294b1868f5641e921e5d5da312c0bdd4839723ee5d09143412b8d554a69b24b3cd5fa08517ad4862ecdaef5c0e74cbf45ed2a9bb7ccefe6b0723dbcdf76910cfbc6ff8889a86365013020fd52767e88f2f058109fe09ffbcc0e312f714ac1f7edfb66370720e9d1ed109796046228c862106295a9971f012f73cfcfdcdde008f72828842360a099119af8982a4e97187e681bad9ab76652a63be6c5b03772123440f495541d0fc2a5f5c6e45db16101f6b1195cecd3134790e66dfedcb0fc59338a6978ce8cd25786f0ec4ceaeb1738b3baa07fab5571ed2db8ed1928a5fb23cbc89965851db7a28a61b34b88680efa97ca058e7f239efdebb65b84768b7057c2731150d51db7a28a61b34b8e735c06f6d0b8246ae132712ba9af02552fbc2408629ff703d0285c5a2127bd2aacb47137167b845cfe06856cc10405945e14f0dead2f91593a0328a567ef3f8c43fb839ffdfaa4031da97447286ed3e5b2ff87aeea91a4341590f0af2765ad8ba57ff74c4764fb64418ddce10178bde802977938e1737435faf049ef419c3a02e83922653eec0e43e1ded422dda8949cf16b0eb8631ad13ca76e01f16dd42d62d2a04e05289cca0&s=%7B%22down_x%22%3A%22cb_down_x%22%2C%22down_y%22%3A%22cb_down_y%22%2C%22up_x%22%3A%22cb_up_x%22%2C%22up_y%22%3A%22cb_up_y%22%7D\"],\"admt\":1,\"adi\":\"http://img1.360buyimg.com/pop/jfs/t1/63216/28/10412/37614/5d7e24b7Ecc999ea7/7b521da3022b2a5a.jpg\",\"deeplinkurl\":\"openapp.jdmobile://virtual?params=%7B%22SE%22%3A%22ADC_WEWKwzCZbMZ%2F4AohacTZymPzqB3IMUs9REA5nADvc1RNsNJqM5obvBhSD7kUknw5Cbn6Bcaz4INvfFWpnOQjyBdGjbeXyDREURz8rCgvH09%2BQOSQ6qKtlKCabaYrcvNaPo3YfgQim9%2BP1YYWCo781OEXRhRmY4wJZNMvZC3uRGQ%3D%22%2C%22action%22%3A%22to%22%2C%22category%22%3A%22jump%22%2C%22des%22%3A%22getCoupon%22%2C%22ext%22%3A%22%7B%5C%22ad%5C%22%3A%5C%22%5C%22%2C%5C%22ch%5C%22%3A%5C%22%5C%22%2C%5C%22shop%5C%22%3A%5C%22%5C%22%2C%5C%22sku%5C%22%3A%5C%22%5C%22%2C%5C%22ts%5C%22%3A%5C%22%5C%22%2C%5C%22uniqid%5C%22%3A%5C%22%7B%5C%5C%5C%22material_id%5C%5C%5C%22%3A%5C%5C%5C%22851051613%5C%5C%5C%22%2C%5C%5C%5C%22pos_id%5C%5C%5C%22%3A%5C%5C%5C%222598%5C%5C%5C%22%2C%5C%5C%5C%22sid%5C%5C%5C%22%3A%5C%5C%5C%22111_4f1bfd215e764f708b1a1c60974d2d6b_1%5C%5C%5C%22%7D%5C%22%7D%22%2C%22kepler_param%22%3A%7B%22channel%22%3A%222e3b9ecfb3a1465badbbbeb48df4140c%22%2C%22source%22%3A%22kepler-open%22%7D%2C%22m_param%22%3A%7B%22jdv%22%3A%22238571484%7Crgyun%7Ct_1000113567_851051613_2598%7Cadrealizable%7C_2_app_0_52636ee66d7d4957aea67e0d402d11ef-p_2598%22%7D%2C%22sourceType%22%3A%22adx%22%2C%22sourceValue%22%3A%22rgyun_111%22%2C%22url%22%3A%22https%3A%2F%2Fccc-x.jd.com%2Fdsp%2Fnc%3Fext%3DaHR0cHM6Ly9zaG9wLm0uamQuY29tLz9zaG9wSWQ9NzkyMjM5JlBUQUc9MTcwNTEuOS4xJmFkX29kPTE%26log%3DMgtoe8BUAyMbZDGYXsVetJGsKFEHzsIL6kUA39e0Pn29DMVkiGXK5aXvA7xbDDgiEVvkaWBPrhJiBFYTdNVtL88aRM3nL7DYjAebGVWmT9LcDJ2lqIKY3YBFhGYUp30kYQd8sYtUlSdZyKou69z6PfRlUdIJ-MUpvNMWOtRgLzQ-4d4qhYnK-xst4WHEeolWFM2IV_aaYm_9ss2jrz-1OYchnCFueCZrg1FRj0qR4LoeorcTJVbHSM2mhSogGpBJ_X6Wcp_v1EdaTcfwZxtklwLKrTl8CQEuFs2mxRx_djjznjUf3mgm-xHg2kGdxZwX7aTm8wMiAog1TIdV_4cfrhZ0NGwt9TZoMA5S7M8kkh9PaKWxy0syV6GdJslA1bZxogti20sR7TWgM2YVHghg05-sSKIrIBZg5OZKJFg1ersY4jopLQzgaxuFkYbOm5rUc17yNCnjMmyGblp-fbzQisecskGug0MxIhDNnfxKjyEqj_zVOEQxti8GyBeaYmquLML0IdyGQsWNeDgkijtB2LohIwlOGFHyTR1_9EbCY9S_iJER9t0dDTMz6-HIWCzteR7VabsWPzy8Uo9EoQT66M7e8-ZnbaosOTgoS9sSNLU%26v%3D404%26SE%3D1%22%7D%0A\",\"adct\":1,\"imptrackers\":[\"http://uat1.bfsspadserver.8le8le.com/adShow?v=4&b=false&i=15689729089370060292273123&r=1001879&bid=111_4f1bfd215e764f708b1a1c60974d2d6b_1&p=2050.0&l=0&mp=3a5ef7e7b111cdd26e42fe8e76337966&d=68928&req=22484853441077249&resp=224848534410772491&u=99000877591185&s=800144&tr=1002258&tm=800166&drid=&deal=&t=1568972908939\",\"http://wn.x.jd.com/adx/nurl/rgyun?price=_8BET0-z9qldVGgsA56IpA&v=100&ad=2598&info=Kn8KJjExMV80ZjFiZmQyMTVlNzY0ZjcwOGIxYTFjNjA5NzRkMmQ2Yl8xIG8oiCcyEjIyNDg0ODUzNDQxMDc3MjQ5MTil7qnZaUCAAkimFFABaN2I6JUDcAB6Djk5MDAwODc3NTkxMTg1gAEIigESY29tLnBpYW5vLmppYW5wYW4w\",\"http://im-x.jd.com/dsp/np?log=Mgtoe8BUAyMbZDGYXsVetJGsKFEHzsIL6kUA39e0Pn29DMVkiGXK5aXvA7xbDDgiEVvkaWBPrhJiBFYTdNVtL88aRM3nL7DYjAebGVWmT9LcDJ2lqIKY3YBFhGYUp30kYQd8sYtUlSdZyKou69z6PfRlUdIJ-MUpvNMWOtRgLzQ-4d4qhYnK-xst4WHEeolWxjD7hM_blYkeelMh7q373XdzoT0CMFXyU0RABem87kMDRUBjla-HyhoVR6h06gw6l7jEV2SsnNNJItkfytTSpYbJIwli8FqOlbA1QQskALHYTZkRkKeamn3f26PTxv52Bhwwo-t_kwDIitPlqKnhLtuU9eYVST739VIFciSqgbOWtyIleAfjATnodf6Dr2fmlgrWSojg00iL7YcL3pPRm9BY_ldpu5jEdvIVwVEKRnBkz6blCTIIT7U9BPl8eIlEDVvX9U_iVrxweGI2rBduqDSTgdDMA2tvo0q6LChFcadR6Es2Zn_PPf5Um5Ux3Q-LqVz3k2ztfZdQo2_V7QVBa0ubF_3hJsCPIHLMztQHueXnDaCcWnZiisGEOZRxin-y&v=404&seq=1&n=rgyun&p=_8BET0-z9qldVGgsA56IpA\",\"http://adx.cloud-cube.net/adx/AdxPvClick?bidid=15689729089370060292273123&type=1&tagid=930&uid=31\",\"http://data.wedooapp.com:6699/106001?enc=f2b743535d5cd195d26cb4e5d4b587cb7af29a3b9d0fb1fac2024e7ff32ac8614d99a215294b1868f5641e921e5d5da312c0bdd4839723ee5d09143412b8d554a69b24b3cd5fa08517ad4862ecdaef5c0e74cbf45ed2a9bb7ccefe6b0723dbcdf76910cfbc6ff8889a86365013020fd52767e88f2f058109fe09ffbcc0e312f714ac1f7edfb66370720e9d1ed109796046228c862106295a9971f012f73cfcfdcdde008f72828842360a099119af8982a4e97187e681bad9ab76652a63be6c5b03772123440f495541d0fc2a5f5c6e45db16101f6b1195cecd3134790e66dfedcb0fc59338a6978ce8cd25786f0ec4ceaeb1738b3baa07fab5571ed2db8ed1928a5fb23cbc89965851db7a28a61b34b88680efa97ca058e7f239efdebb65b84768b7057c2731150d51db7a28a61b34b8e735c06f6d0b8246ae132712ba9af02552fbc2408629ff703d0285c5a2127bd2aacb47137167b845cfe06856cc10405945e14f0dead2f91593a0328a567ef3f8c43fb839ffdfaa4031da97447286ed3e5b2ff87aeea91a4341590f0af2765ad8ba57ff74c4764fb64418ddce10178bde802977938e1737435faf049ef419c3a02e83922653eec0e43e1ded422dda8949cf16b0eb8631ad13ca76e01f16dd42d6c3bb7ac472b1a313\"],\"targeturl\":\"http://uat1.bfsspadserver.8le8le.com/dplClick?v=1&b=false&st=900000&p=&g=https%3A%2F%2Fccc-x.jd.com%2Fdsp%2Fnc%3Fext%3DaHR0cHM6Ly9zaG9wLm0uamQuY29tLz9zaG9wSWQ9NzkyMjM5JlBUQUc9MTcwNTEuOS4xJmFkX29kPTE%26log%3DMgtoe8BUAyMbZDGYXsVetJGsKFEHzsIL6kUA39e0Pn29DMVkiGXK5aXvA7xbDDgiEVvkaWBPrhJiBFYTdNVtL88aRM3nL7DYjAebGVWmT9LcDJ2lqIKY3YBFhGYUp30kYQd8sYtUlSdZyKou69z6PfRlUdIJ-MUpvNMWOtRgLzQ-4d4qhYnK-xst4WHEeolWFM2IV_aaYm_9ss2jrz-1ORJOdMNF5wiS7nA5qdn8f3DWXFuN7nMZD22570z2SGhNUSbWWhJFYNZjxxt3dnZsD4igNJok9Obqzk4BJhY9PXGRneqmmRo3KTX4E4CdzI-NJeAyvI6bbH_Rk39xUmb2axabpNDyonmg3G7WkCvplnHAzo_ajMUEuGz0puJT6dORR8yXZAY55oLGc2wMdgtGoyiDAkK5C8v0Djl4aPeXjuQQh2KGYlLHxbvyyuEitBcrFYEQwm5jQL5cNSiNdTY3FPyEIF7j--eos4_4ukcWZ7xNsgjeFxkBdMNMejQCxQgaKCCV2UlpBrW1fYR3NiqSYnL-bc9odp1-TmAccuco05MHhvueLSMfew5X58625nbw%26v%3D404&i=15689729089370060292273123&r=1001879&d=68928&req=22484853441077249&resp=224848534410772491&u=99000877591185&s=800144&dp=01&drid=&t=1568972908939\"},\"adid\":\"10001\",\"price\":0.0,\"w\":0,\"h\":0,\"id\":\"5175369f-f900-4bcb-b9d3-ea05c0125a67\",\"impid\":\"47ce17aa840143049c2f9607604949aa\",\"cid\":\"A117401000002\"}],\"group\":0}],\"nbr\":200}";
                bidResponse = JSON.parseObject(json, BidResponse.class);
                bidResponse.setId(bidRequest.getId());
            }

            // 获取广告成功
            if (null != bidResponse && bidResponse.getNbr() == 200) {
                //map = BeanMap.create(bidResponse);
                //响应实例
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
            } else {
                response.setStatus(HttpStatus.NO_CONTENT.value());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return map;
    }
}
