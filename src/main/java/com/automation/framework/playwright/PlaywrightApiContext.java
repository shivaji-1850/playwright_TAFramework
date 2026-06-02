package com.automation.framework.playwright;

import com.automation.framework.abstractions.ApiResponse;
import com.automation.framework.abstractions.IApiContext;
import com.automation.framework.logging.FrameworkLogger;
import com.microsoft.playwright.*;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PlaywrightApiContext implements IApiContext {

    private static final Logger log = FrameworkLogger.getLogger(PlaywrightApiContext.class);

    private final APIRequestContext apiContext;
    private String baseUrl = "";
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();
    private Object requestBody;

    public PlaywrightApiContext(Playwright playwright, String baseUrl) {
        this.baseUrl = baseUrl;
        this.apiContext = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL(baseUrl));
        log.info("API Context initialized for baseUrl: {}", baseUrl);
    }

    @Override public IApiContext baseUrl(String url)             { this.baseUrl = url; return this; }
    @Override public IApiContext header(String k, String v)      { headers.put(k, v); return this; }
    @Override public IApiContext bearerToken(String token)       { headers.put("Authorization", "Bearer " + token); return this; }
    @Override public IApiContext queryParam(String k, String v)  { queryParams.put(k, v); return this; }
    @Override public IApiContext body(Object body)               { this.requestBody = body; return this; }

    @Override
    public ApiResponse get(String path) {
        log.info("GET {}{}", baseUrl, path);
        APIRequestContext.FetchOptions opts = buildOptions();
        return new PlaywrightApiResponse(apiContext.get(path, opts));
    }

    @Override
    public ApiResponse post(String path) {
        log.info("POST {}{}", baseUrl, path);
        APIRequestContext.FetchOptions opts = buildOptions();
        return new PlaywrightApiResponse(apiContext.post(path, opts));
    }

    @Override
    public ApiResponse put(String path) {
        log.info("PUT {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.put(path, buildOptions()));
    }

    @Override
    public ApiResponse patch(String path) {
        log.info("PATCH {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.patch(path, buildOptions()));
    }

    @Override
    public ApiResponse delete(String path) {
        log.info("DELETE {}{}", baseUrl, path);
        return new PlaywrightApiResponse(apiContext.delete(path, buildOptions()));
    }

    @Override
    public void dispose() { apiContext.dispose(); }

    private APIRequestContext.FetchOptions buildOptions() {
        APIRequestContext.FetchOptions opts = new APIRequestContext.FetchOptions();
        if (!headers.isEmpty()) opts.setHeaders(headers);
        if (requestBody != null) opts.setData(requestBody.toString());
        // Reset per-request body
        requestBody = null;
        return opts;
    }
}
