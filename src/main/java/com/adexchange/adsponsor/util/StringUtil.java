package com.adexchange.adsponsor.util;



import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class StringUtil {
    public static String md5(String value) {
        return DigestUtils.md5Hex(value).toUpperCase();
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        // 去掉"-"符号
        return uuid.toString().replaceAll("-", "");
    }
}
