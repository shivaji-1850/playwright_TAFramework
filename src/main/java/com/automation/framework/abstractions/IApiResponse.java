package com.automation.framework.abstractions;

import com.microsoft.playwright.APIResponse;

import java.util.Map;

public interface IApiResponse {
    APIResponse getRawResponse();
    int statusCode();
    String body();
    <T> T as(Class<T> clazz);
    String header(String name);
    Map<String, String> headers();
    boolean isOk();         // 2xx
    boolean isClientError(); // 4xx
    boolean isServerError(); // 5xx
}

