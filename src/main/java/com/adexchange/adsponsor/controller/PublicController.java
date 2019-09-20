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
    public Map<String,Object> openRTB(@PathVariable("token") String token, @RequestBody BidRequest bidRequest) {

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
                //log.debug(JSON.toJSONString(bidRequest));
                String json = "{\"cur\":\"CNY\",\"id\":\"32052.001-42.1q3vwqf.321122081.py4usv.db36.620056.妈妈宝典-Android-Banner\",\"seatbid\":[{\"bid\":[{\"ext\":{\"adt\":\"赞助广告\",\"deeplinktrackers\":[\"http://third.mchang.cn/thirdparty/mchang/advertisement/dplink?cid=500600&adspaceid=1541547287930&mcadid=b2f66bbb-160d-483e-9057-f866eb9fc5b4\",\"http://139.199.102.235:16600/106001?enc=e603930f62635d2f23dc008cfcbcc8c24f170527ae1aec4e03d7b432e6b14050c879cb8f7b452924f5641e921e5d5da3eeeca534735df43dec982aa8dcf1b83dd34516b2067cc966e62bcf54d9f964d4a2b0f1a1bd3548236e0e20a0e22e785824379ccc2a653ae93248aecf2ec0ddf3a7158b6d82d6d645512adb90ea8c6a2a7f10fa086b69c07c3b353dcdf76e6bef1989b73969aa007ac0954c1ad3d2df84e9905bd12bb42c6441d1bd12e46c00f0a56519e7f0aa69064513b4ca602efa263746116e253be447d45d972071d08ef5a031b581cd639e4c2833c8053155db5730b6a30fb462e57dc40d4c5bab82711389007da055667b822f08969495f3432da0aacf1b6886f33c19c269f968e1162b7dbe0f9899bd74eb163e2e02cb7e86e5e6b055e336d3e7cf94aa1650ccfa4d9280d23d7a3c11436f4232913f48396a6c178d040677395cc20df7c0a54d381591ec756723e3dcb68f1d071f0a113717ead775643e7fe6e0709c83d280a588d148b164460bc66de19c959ab8f9bf02fd3a7bd37ef703a2d183b529975a7210799eb3a8abba96ffe040b8a92f874edcd044f25271c55bbdba15f927da9b4a1e17c71e03e78224d5503a5e50abbcb45ba7091e744f67add00a6227fdf38fa3f3ac3b6268c43439e12a5b3f6d67a1126020bf0534016175e7e80e\"],\"ads\":\"点击了解更多\",\"clicktrackers\":[\"http://clickc.admaster.com.cn/c/a115727,b2907830,c1572,i0,m101,8a2,8b2,h\",\"http://et.w.inmobi.cn/c.asm/HDaW4MeM6L-9BRaAjQsUWBTIAhWqDBsEWCAoZmQ2YWNmYWI4NzYzMzI3YWVkOTAxMzcyZWZiZWU4NGE4YzZkY2FjNQAgZTBiYWNmNGU4YjI0YmIxMWM0YmQ3NWZhOTE3YjNjMmYeIDQ0MzlmNzBhNDExYjg1MWRhZTM2OTFjYTdiZjNhNzYwBCgyYTk2MzQ3Mzg1YTE2MDczYjc1NTlkODFjMWEzZjZmZGU3NTlmZGUyHBUEFRgVAhXsBBUCJQAoA2RpcgATABwXqaROQBOBPkAXV-wvuydDXUAAFQAYDFkyNHViV05vWVc1bhbQg_sCHBwWgMDolqDJnPedARaZ_f__n5Xf_zcAABUeFwAAAAAAgFFAJAAcEhkFABbQg_sCGANVU0QW9JWHtt1ZGfUeFLwDghu-PphOgn2qrAGsrAGurAG4rQGArwHSuwHWuwHC7QGK7wGM7wHS8AHQiQLSiQLmmQKQmgKamgLgmgLg1wK8_QLcggO2hwO4hwPypQOgkwQhGE0MAAELAAEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKAAIAAAAAABcueAAEAAJACMKPXCj1wwoABAAAAAAANdHhADgiBAABQFGAAAAAAAAEAAJACAAAAAAAAAwAAwgAAQAAAAUAABhtAwABBAgAAgAAAA0GAAUCgAYABgRwCwAHAAAABTMuMS4wCwAIAAAAA0FQUAgACQAAAAECAAoACAALAAQfeAQADEAAzMzMzMzNCAANAA6UVwgADgAAcHsLAA8AAAAPREVSSVZFRF9MQVRfTE9OABi4AQoAAUZSeXEfcK6DCwACAAAAoAsAAQAAAAljbi5tY2hhbmcDAAIAAgAEAQIABQAGAAYQEAsABwAAAARSVEJECwAJAAAAIDlhMTUxMzM2Zjg2NzQzMjE5OWE5YTMyOTc5MTY4NmEwCAAOAAJLKgsADwAAAAczMjBYNTY4BgAQAAALABEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKABIAAAAAAKi8ZgAGAAQAAAAAGJICNvSVh7bdWag4RHB1bE9xMTkxdTZxVm5Fam9BMkhmOUFPMDM1YjRISExDVk8yTjljVDhnNXVrdDN2dUJmT1pRPT0YBk5BVElWRRw8HBaAwOiWoMmc950BFpn9__-fld__NwAWhrqF96PcvNKMASIAADn1HoCvAYJ9ghuK7wGM7wGQmgIUmE6amgKgkwSqrAGsrAGurAG2hwO4rQG4hwO8_QK8A74-wu0B0IkC0vAB0rsB0okC1rsB3IID4JoC4NcC5pkC8qUDHACW8P0gHBUAACwVAACFAhEsFoDA6JagyZz3nQEWmf2f9IqV35k3ACXKARUKOBduYXRpdmVfODY0MjQ2NTAxNTI3NTMwODQGWRQCABgBNwA/959d9928?m=8&ts=$TS\",\"https://c.w.inmobi.cn/c.asm/HDaW4MeM6L-9BRaAjQsUWBTIAhWqDBsEWCAoZmQ2YWNmYWI4NzYzMzI3YWVkOTAxMzcyZWZiZWU4NGE4YzZkY2FjNQAgZTBiYWNmNGU4YjI0YmIxMWM0YmQ3NWZhOTE3YjNjMmYeIDQ0MzlmNzBhNDExYjg1MWRhZTM2OTFjYTdiZjNhNzYwBCgyYTk2MzQ3Mzg1YTE2MDczYjc1NTlkODFjMWEzZjZmZGU3NTlmZGUyHBUEFRgVAhXsBBUCJQAoA2RpcgATARwXqaROQBOBPkAXV-wvuydDXUAAFQAYDFkyNHViV05vWVc1bhbQg_sCHBwWgMDolqDJnPedARaZ_f__n5Xf_zcAABUeFwAAAAAAgFFAJAAcEhkFABbQg_sCGANVU0QW9JWHtt1ZGfUeFLwDghu-PphOgn2qrAGsrAGurAG4rQGArwHSuwHWuwHC7QGK7wGM7wHS8AHQiQLSiQLmmQKQmgKamgLgmgLg1wK8_QLcggO2hwO4hwPypQOgkwQhGE0MAAELAAEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKAAIAAAAAABcueAAEAAJACMKPXCj1wwoABAAAAAAANdHhADgiBAABQFGAAAAAAAAEAAJACAAAAAAAAAwAAwgAAQAAAAUAABhtAwABBAgAAgAAAA0GAAUCgAYABgRwCwAHAAAABTMuMS4wCwAIAAAAA0FQUAgACQAAAAECAAoACAALAAQfeAQADEAAzMzMzMzNCAANAA6UVwgADgAAcHsLAA8AAAAPREVSSVZFRF9MQVRfTE9OABi4AQoAAUZSeXEfcK6DCwACAAAAoAsAAQAAAAljbi5tY2hhbmcDAAIAAgAEAQIABQAGAAYQEAsABwAAAARSVEJECwAJAAAAIDlhMTUxMzM2Zjg2NzQzMjE5OWE5YTMyOTc5MTY4NmEwCAAOAAJLKgsADwAAAAczMjBYNTY4BgAQAAALABEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKABIAAAAAAKi8ZgAGAAQAAAAAGJICNvSVh7bdWag4RHB1bE9xMTkxdTZxVm5Fam9BMkhmOUFPMDM1YjRISExDVk8yTjljVDhnNXVrdDN2dUJmT1pRPT0YBk5BVElWRRw8HBaAwOiWoMmc950BFpn9__-fld__NwAWhrqF96PcvNKMASIAADn1HoCvAYJ9ghuK7wGM7wGQmgIUmE6amgKgkwSqrAGsrAGurAG2hwO4rQG4hwO8_QK8A74-wu0B0IkC0vAB0rsB0okC1rsB3IID4JoC4NcC5pkC8qUDHACW8P0gHBUAACwVAACFAhEsFoDA6JagyZz3nQEWmf2f9IqV35k3ACXKARUKOBduYXRpdmVfODY0MjQ2NTAxNTI3NTMwODQGWRQCABgBNwA/325fb3e5?at=0&am=7&ct=$TS\",\"http://third.mchang.cn/thirdparty/mchang/advertisement/click?p_q_c=eyJNY2FkaWQiOiJiMmY2NmJiYi0xNjBkLTQ4M2UtOTA1Ny1mODY2ZWI5ZmM1YjQiLCJjb25uIjoiMSIsIm1hYyI6IkQwRkY5OEIwODcyMSIsImFpZCI6IjljZWM0NWU5YmU3MWZkZjkiLCJhZGkiOiJiMmY2NmJiYi0xNjBkLTQ4M2UtOTA1Ny1mODY2ZWI5ZmM1YjQiLCJkYXRlIjoiMjAxOTA5MjAiLCJjaGFubmVsaWQiOiI1MDA2MDAiLCJjbGlja2ludGVyY2VwdCI6ZmFsc2UsInRpbWUiOjE1Njg5ODc4ODc4NDAsImdnaWQiOiIzNTZkNmViNDcwYTNiMjdjNDM3YjdmNGE4YWJjOGFjNSIsImN1ckhvdXIiOiIyMSIsImNhcnJpZXIiOiIxIiwic1BrZ05hbWUiOiIiLCJpbWd1cmwiOiI5OWNkMmUwYjM0ODg3MmIwIiwib3MiOiIwIiwib3N2IjoiNi4wIiwiaW1laSI6Ijg2NzE3MDAzODQ2MTk3OSIsImFkdHlwZSI6IjQiLCJhZHNwYWNlaWQiOiIxNTQxNTQ3Mjg3OTMwIiwicGtnbmFtZSI6ImNuLnl5c2luZyIsImFwaXZlcnNpb24iOiIzLjAiLCJpcCI6IjIyMy4yMTUuODIuMTE5IiwiaW1nVXJsIjoiaHR0cCUzQSUyRiUyRmltZzEuMzYwYnV5aW1nLmNvbSUyRnBvcCUyRmpmcyUyRnQxJTJGNzAxNTYlMkY3JTJGMTA1NjMlMkY5OTExMCUyRjVkODFhNTcxRWE2M2I0Nzc3JTJGZDYxNzA4NjRmZjk5NTY5My5qcGciLCJhcHBuYW1lIjoiJUU5JUJBJUE2JUU1JTk0JUIxIiwiZGV2aWNlIjoiSE9OT1IlMkJNWUEtQUwxMCIsInVhIjoiTW96aWxsYSUyRjUuMCslMjhMaW51eCUzQitBbmRyb2lkKzYuMCUzQitNWUEtQUwxMCtCdWlsZCUyRkhPTk9STVlBLUFMMTAlM0Ird3YlMjkrQXBwbGVXZWJLaXQlMkY1MzcuMzYrJTI4S0hUTUwlMkMrbGlrZStHZWNrbyUyOStWZXJzaW9uJTJGNC4wK0Nocm9tZSUyRjQzLjAuMjM1Ny42NStNb2JpbGUrU2FmYXJpJTJGNTM3LjM2IiwiY2xpY2tVcmwiOiJodHRwcyUzQSUyRiUyRmNjYy14LmpkLmNvbSUyRmRzcCUyRm5jJTNGZXh0JTNEYUhSMGNITTZMeTl3Y205a1pYWXViUzVxWkM1amIyMHZiV0ZzYkM5aFkzUnBkbVV2TTBNemFHVlFTa2RVV2pab1pUUlJhbFJLUWtORVIydEVTMWRUZVM5cGJtUmxlQzVvZEcxc1AxQlVRVWM5TVRjd05URXVPUzR4Sm1Ga1gyOWtQVEUlMjZsb2clM0RYUjZRVjRqVjBTYV84S0Nzc2IyeDdCcktIVzhfN01uN09KT0w3b3NWLUx4TXNyejlxNmREUnQ5Q3IyS2x3eVotVFFfank2QW5aNm1FVTNuSFhEMW1DV20wVUE3b1JPbzBvUkhQQkl0NUwwQ1ZJek5GcWs3cm5QbVVUYnd2aDJWUmExN2trZVV2c1pUSHAtNDJCVncydnRNYUZvYkpodExCakRBeUgtSjd4d1k1UVpHRTBzc3BUVnVYU3A3eWlVS1hrVlRGMFUxd25janRzbDJYNE5CM25JUGhUOHI0di1MdUc5cUNEYTBnM0ZEVTZVUjFuOVlfT0ZsbE5CbDFWV1VKU0VRTGZDUlZFMVVmQmxDMGRxOGQxVThhV2ZYVms1Q1BadG9RaklpQ0FVRlhpLUNaWURFc2VmMkhKSVJfcVFJSmFqVWUzZmI0U1F0bXVCampnc0dBb01YZE5GOGVpblNCcWJTRGJnb3h1aExNdi1jZTA1S0xRb1A4UjBSUmFsamZ2SUJfSkVDNTFva3MzR1lJYVN3dUIwRVR6VUhFbTlFZEN4QmtkbkJkbURJWVRYM1dNNnNMTFotdDJWTThhT3VkS0dfMm85QUk3OXFWY0xYTW9JdXlhcXM0aE12a3ktbE1UR3F0ZXdBWUZBVEdNMWhmVUpmZDUtSFJRMU9WREQ2dE51UkFVaDJzVWNPSTE4YWkzOEVwNlZpNUFWcVhGbXZNRHlrbUlIOWN3YXNadGNEZWdRUW9CaXE5NGV6WlE4STUyN1lJdzY5MFFkSmlNclU5TW5PcXBxQ0JSd3lqeGRNOUhyN3hVVUFIWVc4JTI2diUzRDQwNCIsImltc2kiOiI0NjAwNzQzNTA0MTgxMDMiLCJpc0dldEFkRHBsaW5rIjpmYWxzZX0=&adid=6242b37bcdb6a5f2d7a2d571ce2a4de1&xpclick=00&r_click_d_x=-999&r_click_d_y=-999&r_click_u_x=-999&r_click_u_y=-999&a_click_d_x=-999&a_click_d_y=-999&a_click_u_x=-999&a_click_u_y=-999\",\"http://139.199.102.235:16600/106001?enc=e603930f62635d2f23dc008cfcbcc8c24f170527ae1aec4e03d7b432e6b14050c879cb8f7b452924f5641e921e5d5da3eeeca534735df43dec982aa8dcf1b83dd34516b2067cc966e62bcf54d9f964d4a2b0f1a1bd3548236e0e20a0e22e785824379ccc2a653ae93248aecf2ec0ddf3a7158b6d82d6d645512adb90ea8c6a2a7f10fa086b69c07c3b353dcdf76e6bef1989b73969aa007ac0954c1ad3d2df84e9905bd12bb42c6441d1bd12e46c00f0a56519e7f0aa69064513b4ca602efa263746116e253be447d45d972071d08ef5a031b581cd639e4c2833c8053155db5730b6a30fb462e57dc40d4c5bab82711389007da055667b822f08969495f3432da0aacf1b6886f33c19c269f968e1162b7dbe0f9899bd74eb163e2e02cb7e86e5e6b055e336d3e7cf94aa1650ccfa4d9280d23d7a3c11436f4232913f48396a6c178d040677395cc20df7c0a54d381591ec756723e3dcb68f1d071f0a113717ead775643e7fe6e0709c83d280a588d148b164460bc66de19c959ab8f9bf02fd3a7bd37ef703a2d183b529975a7210799eb3a8abba96ffe040b8a92f874edcd044f25271c55bbdba15f927da9b4a1e17c71e03e78224d5503a5e50abbcb45ba7091e744f67add00a6227fdf38fa3f3ac3b6268c43439e12a5b3f6d67a1126020bf35ea4f65958e51f4&s=%7B%22down_x%22%3A%22cb_down_x%22%2C%22down_y%22%3A%22cb_down_y%22%2C%22up_x%22%3A%22cb_up_x%22%2C%22up_y%22%3A%22cb_up_y%22%7D\"],\"admt\":1,\"adi\":\"http://img1.360buyimg.com/pop/jfs/t1/70156/7/10563/99110/5d81a571Ea63b4777/d6170864ff995693.jpg\",\"deeplinkurl\":\"openapp.jdmobile://virtual?params=%7B%22SE%22%3A%22ADC_kR6xhi2lRFSzADj0jmqU%2B7HvpCBwANFI9Udqf7S%2BRnhn796XfFQ0Z2R7Dtoe7A9j%2Fvsk2rZKJ%2F48L9V3BVn3iRb1S4wqXvzQ%2FHh7bh0cMispPxRvlyGK6JPe626Lasg9YhTWlLFIGTEOkWu8uPB8GvGVEgTUdL7MTHXPsBGHEaI%3D%22%2C%22action%22%3A%22to%22%2C%22category%22%3A%22jump%22%2C%22des%22%3A%22getCoupon%22%2C%22ext%22%3A%22%7B%5C%22ad%5C%22%3A%5C%22%5C%22%2C%5C%22ch%5C%22%3A%5C%22%5C%22%2C%5C%22shop%5C%22%3A%5C%22%5C%22%2C%5C%22sku%5C%22%3A%5C%22%5C%22%2C%5C%22ts%5C%22%3A%5C%22%5C%22%2C%5C%22uniqid%5C%22%3A%5C%22%7B%5C%5C%5C%22material_id%5C%5C%5C%22%3A%5C%5C%5C%22864246501%5C%5C%5C%22%2C%5C%5C%5C%22pos_id%5C%5C%5C%22%3A%5C%5C%5C%222643%5C%5C%5C%22%2C%5C%5C%5C%22sid%5C%5C%5C%22%3A%5C%5C%5C%2273_89900529d5f74eef845ffcb6e88bae85_1%5C%5C%5C%22%7D%5C%22%7D%22%2C%22kepler_param%22%3A%7B%22channel%22%3A%222e3b9ecfb3a1465badbbbeb48df4140c%22%2C%22source%22%3A%22kepler-open%22%7D%2C%22m_param%22%3A%7B%22jdv%22%3A%22238571484%7Cinmobi%7Ct_1000011159_864246501_2643%7Cadrealizable%7C_2_app_0_94aab1d09f1746c18fd8fd560c560a87-p_2643%22%7D%2C%22sourceType%22%3A%22adx%22%2C%22sourceValue%22%3A%22inmobi_73%22%2C%22url%22%3A%22https%3A%2F%2Fccc-x.jd.com%2Fdsp%2Fnc%3Fext%3DaHR0cHM6Ly9wcm9kZXYubS5qZC5jb20vbWFsbC9hY3RpdmUvM0MzaGVQSkdUWjZoZTRRalRKQkNER2tES1dTeS9pbmRleC5odG1sP1BUQUc9MTcwNTEuOS4xJmFkX29kPTE%26log%3DXR6QV4jV0Sa_8KCssb2x7BrKHW8_7Mn7OJOL7osV-LxMsrz9q6dDRt9Cr2KlwyZ-TQ_jy6AnZ6mEU3nHXD1mCWm0UA7oROo0oRHPBIt5L0CVIzNFqk7rnPmUTbwvh2VRa17kkeUvsZTHp-42BVw2vtMaFobJhtLBjDAyH-J7xwY5QZGE0sspTVuXSp7yiUKXp-zgSmY6HojH16dYgq6DYEaND1LUDw46OIpT__HiLUCDi-cEx3MhjfJJ_nq-Vj2T97WUrJqrzKxGcn45OOsiY3g19hj9EuDUuby0MnHR-YsS97iZ98LQGTtLgFcWmz-bONLMKucxRi2oFAX65TBmyCGcr1rajcNgowJRlBoS8L2n8SRqcmxFW9EuerxNpFTAyrnAjtAid-ekrSJvOE1VYrSEK_D6k-AtYLpFvlIqyW8xV8evnZdeqxIVzvXPuEwilMmA8WDuoNztP9Q-QZydmbh4pkqPL9fPwHJ0vV0oyaWn2RvEuvXcYS6BJhWtBfLIuhdzMnWT-aePHApyVDpNjJoxdrfD88GAGVpiNhBLmR3UD2vOfIPQOr0-z5vq5Pti6fge7Q0iL-z31op1arJ6Qn_2dFJo-IZKCcsutd88mG0h_uoalFb-5tMmSOL3I5c68iHk3zQkNpRO9UZ9Ck-4PGZbj3dpQ8o_HfvHgYlfnoE%26v%3D404%26SE%3D1%22%7D%0A\",\"adct\":1,\"imptrackers\":[\"http://et.w.inmobi.cn/c.asm/HDaW4MeM6L-9BRaAjQsUWBTIAhWqDBsEWCAoZmQ2YWNmYWI4NzYzMzI3YWVkOTAxMzcyZWZiZWU4NGE4YzZkY2FjNQAgZTBiYWNmNGU4YjI0YmIxMWM0YmQ3NWZhOTE3YjNjMmYeIDQ0MzlmNzBhNDExYjg1MWRhZTM2OTFjYTdiZjNhNzYwBCgyYTk2MzQ3Mzg1YTE2MDczYjc1NTlkODFjMWEzZjZmZGU3NTlmZGUyHBUEFRgVAhXsBBUCJQAoA2RpcgATABwXqaROQBOBPkAXV-wvuydDXUAAFQAYDFkyNHViV05vWVc1bhbQg_sCHBwWgMDolqDJnPedARaZ_f__n5Xf_zcAABUeFwAAAAAAgFFAJAAcEhkFABbQg_sCGANVU0QW9JWHtt1ZGfUeFLwDghu-PphOgn2qrAGsrAGurAG4rQGArwHSuwHWuwHC7QGK7wGM7wHS8AHQiQLSiQLmmQKQmgKamgLgmgLg1wK8_QLcggO2hwO4hwPypQOgkwQhGE0MAAELAAEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKAAIAAAAAABcueAAEAAJACMKPXCj1wwoABAAAAAAANdHhADgiBAABQFGAAAAAAAAEAAJACAAAAAAAAAwAAwgAAQAAAAUAABhtAwABBAgAAgAAAA0GAAUCgAYABgRwCwAHAAAABTMuMS4wCwAIAAAAA0FQUAgACQAAAAECAAoACAALAAQfeAQADEAAzMzMzMzNCAANAA6UVwgADgAAcHsLAA8AAAAPREVSSVZFRF9MQVRfTE9OABi4AQoAAUZSeXEfcK6DCwACAAAAoAsAAQAAAAljbi5tY2hhbmcDAAIAAgAEAQIABQAGAAYQEAsABwAAAARSVEJECwAJAAAAIDlhMTUxMzM2Zjg2NzQzMjE5OWE5YTMyOTc5MTY4NmEwCAAOAAJLKgsADwAAAAczMjBYNTY4BgAQAAALABEAAAAgOWExNTEzMzZmODY3NDMyMTk5YTlhMzI5NzkxNjg2YTAKABIAAAAAAKi8ZgAGAAQAAAAAGJICNvSVh7bdWag4RHB1bE9xMTkxdTZxVm5Fam9BMkhmOUFPMDM1YjRISExDVk8yTjljVDhnNXVrdDN2dUJmT1pRPT0YBk5BVElWRRw8HBaAwOiWoMmc950BFpn9__-fld__NwAWhrqF96PcvNKMASIAADn1HoCvAYJ9ghuK7wGM7wGQmgIUmE6amgKgkwSqrAGsrAGurAG2hwO4rQG4hwO8_QK8A74-wu0B0IkC0vAB0rsB0okC1rsB3IID4JoC4NcC5pkC8qUDHACW8P0gHBUAACwVAACFAhEsFoDA6JagyZz3nQEWmf2f9IqV35k3ACXKARUKOBduYXRpdmVfODY0MjQ2NTAxNTI3NTMwODQGWRQCABgBNwA/959d9928?m=18&ts=$TS\",\"http://im-x.jd.com/dsp/np?log=XR6QV4jV0Sa_8KCssb2x7BrKHW8_7Mn7OJOL7osV-LxMsrz9q6dDRt9Cr2KlwyZ-TQ_jy6AnZ6mEU3nHXD1mCWm0UA7oROo0oRHPBIt5L0CVIzNFqk7rnPmUTbwvh2VRa17kkeUvsZTHp-42BVw2vtMaFobJhtLBjDAyH-J7xwY5QZGE0sspTVuXSp7yiUKXcPtZxR_rERnnrZakWqTmNmc_lUAMWmqcIeJ4vaQzHjnvRD-gwBGVMghpdhX0oNrjJP6SOrOZkpkhjL8ZasngZhvnwIyumAhA4t8cleKOSN5KNm1oNuNzUFO2XjVFNrHNdkfvKwjGJGzhlY6a4G1v691LgvKI2Wh05eJMpKjcVsMyAE40ZXtwv-v131hT5xKkr0Obp71cDsFy6VIU2ju7L5uSCoAMMwrCMbYvfO3z--IQWQEuNtd41-rlql8zG7y9neFcSifX5K-KbA63w19QKoOagLqwUZWmNPol6w2fjzt7eEYUedBJXEe_QxgMEX_h07MZvUloQg2n4IM63TltFzkEUpyzPGMLlGV05dJxn2Tx7UX7SpDrBuz70S69ip0UtN0ks1HlXShzRgqJ-sbBtoUm6Vp0gPjByWzhaHR5630&v=404&seq=1&n=inmobi&p=22.007934\",\"http://wn.x.jd.com/adx/nurl/inmobi?price=22.007934&v=100&ad=2643&info=KpgBCiU3M184OTkwMDUyOWQ1Zjc0ZWVmODQ1ZmZjYjZlODhiYWU4NV8xIEkoxBMyJDRlZjczOTI1LTAxNmQtMTAwMC1lNDY2LTQxYWJhOGJjMDBiMzis_cECQIACSNMUUAFo5bWNnANwAHogNDQzOUY3MEE0MTFCODUxREFFMzY5MUNBN0JGM0E3NjCAAUCKAQljbi5tY2hhbmc\",\"http://third.mchang.cn/thirdparty/mchang/advertisement/show?p_q_s=eyJNY2FkaWQiOiJiMmY2NmJiYi0xNjBkLTQ4M2UtOTA1Ny1mODY2ZWI5ZmM1YjQiLCJvcyI6IjAiLCJjb25uIjoiMSIsIm9zdiI6IjYuMCIsImltZWkiOiI4NjcxNzAwMzg0NjE5NzkiLCJhZHR5cGUiOiI0IiwibWFjIjoiRDBGRjk4QjA4NzIxIiwiYWRzcGFjZWlkIjoiMTU0MTU0NzI4NzkzMCIsImFkaSI6ImIyZjY2YmJiLTE2MGQtNDgzZS05MDU3LWY4NjZlYjlmYzViNCIsImFpZCI6IjljZWM0NWU5YmU3MWZkZjkiLCJkYXRlIjoiMjAxOTA5MjAiLCJwa2duYW1lIjoiY24ueXlzaW5nIiwiYXBpdmVyc2lvbiI6IjMuMCIsImlwIjoiMjIzLjIxNS44Mi4xMTkiLCJjaGFubmVsaWQiOiI1MDA2MDAiLCJpbWdVcmwiOiJodHRwJTNBJTJGJTJGaW1nMS4zNjBidXlpbWcuY29tJTJGcG9wJTJGamZzJTJGdDElMkY3MDE1NiUyRjclMkYxMDU2MyUyRjk5MTEwJTJGNWQ4MWE1NzFFYTYzYjQ3NzclMkZkNjE3MDg2NGZmOTk1NjkzLmpwZyIsInRpbWUiOjE1Njg5ODc4ODc4NDAsImdnaWQiOiIzNTZkNmViNDcwYTNiMjdjNDM3YjdmNGE4YWJjOGFjNSIsImN1ckhvdXIiOiIyMSIsImFwcG5hbWUiOiIlRTklQkElQTYlRTUlOTQlQjEiLCJkZXZpY2UiOiJIT05PUiUyQk1ZQS1BTDEwIiwidWEiOiJNb3ppbGxhJTJGNS4wKyUyOExpbnV4JTNCK0FuZHJvaWQrNi4wJTNCK01ZQS1BTDEwK0J1aWxkJTJGSE9OT1JNWUEtQUwxMCUzQit3diUyOStBcHBsZVdlYktpdCUyRjUzNy4zNislMjhLSFRNTCUyQytsaWtlK0dlY2tvJTI5K1ZlcnNpb24lMkY0LjArQ2hyb21lJTJGNDMuMC4yMzU3LjY1K01vYmlsZStTYWZhcmklMkY1MzcuMzYiLCJjYXJyaWVyIjoiMSIsImltc2kiOiI0NjAwNzQzNTA0MTgxMDMiLCJpbWd1cmwiOiI5OWNkMmUwYjM0ODg3MmIwIn0=&adid=f5b2afafb54cea1140fa6c570cd21066&xpshow=00\",\"http://139.199.102.235:16600/106001?enc=e603930f62635d2f23dc008cfcbcc8c24f170527ae1aec4e03d7b432e6b14050c879cb8f7b452924f5641e921e5d5da3eeeca534735df43dec982aa8dcf1b83dd34516b2067cc966e62bcf54d9f964d4a2b0f1a1bd3548236e0e20a0e22e785824379ccc2a653ae93248aecf2ec0ddf3a7158b6d82d6d645512adb90ea8c6a2a7f10fa086b69c07c3b353dcdf76e6bef1989b73969aa007ac0954c1ad3d2df84e9905bd12bb42c6441d1bd12e46c00f0a56519e7f0aa69064513b4ca602efa263746116e253be447d45d972071d08ef5a031b581cd639e4c2833c8053155db5730b6a30fb462e57dc40d4c5bab82711389007da055667b822f08969495f3432da0aacf1b6886f33c19c269f968e1162b7dbe0f9899bd74eb163e2e02cb7e86e5e6b055e336d3e7cf94aa1650ccfa4d9280d23d7a3c11436f4232913f48396a6c178d040677395cc20df7c0a54d381591ec756723e3dcb68f1d071f0a113717ead775643e7fe6e0709c83d280a588d148b164460bc66de19c959ab8f9bf02fd3a7bd37ef703a2d183b529975a7210799eb3a8abba96ffe040b8a92f874edcd044f25271c55bbdba15f927da9b4a1e17c71e03e78224d5503a5e50abbcb45ba7091e744f67add00a6227fdf38fa3f3ac3b6268c43439e12a5b3f6d67a1126020bfd088eb91e82681e6\"],\"targeturl\":\"https://ccc-x.jd.com/dsp/nc?ext=aHR0cHM6Ly9wcm9kZXYubS5qZC5jb20vbWFsbC9hY3RpdmUvM0MzaGVQSkdUWjZoZTRRalRKQkNER2tES1dTeS9pbmRleC5odG1sP1BUQUc9MTcwNTEuOS4xJmFkX29kPTE&log=XR6QV4jV0Sa_8KCssb2x7BrKHW8_7Mn7OJOL7osV-LxMsrz9q6dDRt9Cr2KlwyZ-TQ_jy6AnZ6mEU3nHXD1mCWm0UA7oROo0oRHPBIt5L0CVIzNFqk7rnPmUTbwvh2VRa17kkeUvsZTHp-42BVw2vtMaFobJhtLBjDAyH-J7xwY5QZGE0sspTVuXSp7yiUKXkVTF0U1wncjtsl2X4NB3nIPhT8r4v-LuG9qCDa0g3FDU6UR1n9Y_OFllNBl1VWUJSEQLfCRVE1UfBlC0dq8d1U8aWfXVk5CPZtoQjIiCAUFXi-CZYDEsef2HJIR_qQIJajUe3fb4SQtmuBjjgsGAoMXdNF8einSBqbSDbgoxuhLMv-ce05KLQoP8R0RRaljfvIB_JEC51oks3GYIaSwuB0ETzUHEm9EdCxBkdnBdmDIYTX3WM6sLLZ-t2VM8aOudKG_2o9AI79qVcLXMoIuyaqs4hMvky-lMTGqtewAYFATGM1hfUJfd5-HRQ1OVDD6tNuRAUh2sUcOI18ai38Ep6Vi5AVqXFmvMDykmIH9cwasZtcDegQQoBiq94ezZQ8I527YIw690QdJiMrU9MnOqpqCBRwyjxdM9Hr7xUUAHYW8&v=404\"},\"adid\":\"10001\",\"price\":0.0,\"w\":0,\"h\":0,\"id\":\"f162dd67-1e09-4497-88a7-b3d2cb49ef9e\",\"impid\":\"1\",\"cid\":\"A117401000002\"}],\"group\":0}],\"nbr\":200}";
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

                log.debug("token:{},广告：{}", token, JSON.toJSONString(map));

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
