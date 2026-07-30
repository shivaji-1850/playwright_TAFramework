package com.automation.framework.playwright;

import com.automation.framework.abstractions.IApiResponse;
import com.automation.framework.logging.FrameworkLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.slf4j.Logger;

import java.util.Map;

public class PlaywrightApiResponse implements IApiResponse {
    private final Logger log = FrameworkLogger.getLogger(PlaywrightApiResponse.class);
    private final ObjectMapper objectMapper;
    private final APIResponse apiResponse;

    public PlaywrightApiResponse(APIResponse apiResponse) {
        if (apiResponse == null) {
            throw new IllegalArgumentException("APIResponse cannot be null");
        }
        this.apiResponse = apiResponse;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public APIResponse getRawResponse() {
        return apiResponse;
    }

    @Override
    public int statusCode() {
        return apiResponse.status();
    }

    @Override
    public String body() {
        return apiResponse.text();
    }

    @Override
    public <T> T as(Class<T> clazz) {
        try {
            return objectMapper.readValue(body(), clazz);
        } catch (Exception ex) {
            log.error("Failed to deserialize API response body to {}", clazz.getName(), ex);
            throw new RuntimeException("Failed to deserialize API response body", ex);
        }
    }

    @Override
    public String header(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        Map<String, String> allHeaders = headers();
        if (allHeaders.containsKey(name)) {
            return allHeaders.get(name);
        }

        for (Map.Entry<String, String> entry : allHeaders.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Map<String, String> headers() {
        return apiResponse.headers();
    }

    @Override
    public boolean isOk() {
        int code = statusCode();
        return code >= 200 && code < 300;
    }

    @Override
    public boolean isClientError() {
        int code = statusCode();
        return code >= 400 && code < 500;
    }

    @Override
    public boolean isServerError() {
        int code = statusCode();
        return code >= 500 && code < 600;
    }
}

