package com.adexchange.adsponsor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebResponseResult {
    public Integer statusCode;
    public Integer result;
    public String response;

    public static enum ResultEnum {
        SUCCESS(0, "成功"), FAILURE(1, "失败"), TIMEOUT(2, "超时");
        @Getter
        private int value;
        @Getter
        private String description;

        private ResultEnum(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
