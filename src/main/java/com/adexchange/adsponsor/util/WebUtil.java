package com.adexchange.adsponsor.util;

import com.adexchange.adsponsor.dto.WebResponseResult;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

public class WebUtil {
    private static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient okHttpClient;
    private static OkHttpClient okHttpClientBuilder;

    private static OkHttpClient okHttpInstance() {
        if (okHttpClient == null) {
            synchronized (WebUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    private static OkHttpClient okHttpBuilderInstance() {
        if (okHttpClientBuilder == null) {
            synchronized (WebUtil.class) {
                if (okHttpClientBuilder == null) {
                    okHttpClientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(300, TimeUnit.MILLISECONDS)
                            .writeTimeout(300, TimeUnit.MILLISECONDS)
                            .readTimeout(300, TimeUnit.MILLISECONDS)
                            .build();
                }
            }
        }
        return okHttpClientBuilder;
    }


    public static WebResponseResult HttpRequestGet(String url) {
        WebResponseResult responseResult = new WebResponseResult(200, WebResponseResult.ResultEnum.SUCCESS.getValue(), "");
        try {
            Request request = new Request.Builder()
                    .addHeader("Accept", "*/*")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0" +
                            ".1847.131 Safari/537.36")
                    .url(url)
                    .build();
            Response response = okHttpBuilderInstance().newCall(request).execute();
            Integer statusCode = response.code();
            responseResult.setStatusCode(statusCode);
            if (response.isSuccessful()) {
                if (statusCode == 200) {
                    responseResult.setResponse(response.body().string());
                } else {
                    responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
                }
            } else {
                responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
            }

        } catch (Exception e) {
            responseResult.setResult(WebResponseResult.ResultEnum.TIMEOUT.getValue());
            responseResult.setResponse(e.getMessage());
        }
        return responseResult;
    }

    public static WebResponseResult HttpRequestPost(String url, String jsonParams) {
        WebResponseResult responseResult = new WebResponseResult(0, WebResponseResult.ResultEnum.SUCCESS.getValue(), "");
        try {
            RequestBody requestBody = RequestBody.create(jsonParams, mediaType);
//            RequestBody requestBody = new RequestBody() {
//
//                @Override
//                public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
//                    bufferedSink.writeUtf8(jsonParams);
//                }
//
//                @Nullable
//                @Override
//                public MediaType contentType() {
//                    return MediaType.parse("text/x-markdown; charset=utf-8");
//                }
//            };
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0" +
                            ".1847.131 Safari/537.36")
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpBuilderInstance().newCall(request).execute();
            Integer statusCode = response.code();
            responseResult.setStatusCode(statusCode);
            if (response.isSuccessful()) {
                if (statusCode == 200) {
                    responseResult.setResponse(response.body().string());
                } else {
                    responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
                }
            } else {
                responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
            }

        } catch (Exception e) {
            responseResult.setResult(WebResponseResult.ResultEnum.TIMEOUT.getValue());
            responseResult.setResponse(e.getMessage());
        }
        return responseResult;
    }

    public static WebResponseResult HttpRequestPost(String url, String jsonParams, int timeOut) {
        WebResponseResult responseResult = new WebResponseResult(0, WebResponseResult.ResultEnum.SUCCESS.getValue(), "");
        try {
            RequestBody requestBody = RequestBody.create(jsonParams, mediaType);
            Request request = new Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0" +
                            ".1847.131 Safari/537.36")
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpInstance().newBuilder()
                    .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .build().newCall(request).execute();
            Integer statusCode = response.code();
            responseResult.setStatusCode(statusCode);
            if (response.isSuccessful()) {
                if (statusCode == 200) {
                    responseResult.setResponse(response.body().string());
                } else {
                    responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
                }
            } else {
                responseResult.setResult(WebResponseResult.ResultEnum.FAILURE.getValue());
            }

        } catch (Exception e) {
            responseResult.setResult(WebResponseResult.ResultEnum.TIMEOUT.getValue());
            responseResult.setResponse(e.getMessage());
        }
        return responseResult;
    }
}
